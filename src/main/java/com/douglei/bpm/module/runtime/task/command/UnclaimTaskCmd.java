package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskEntity;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.orm.context.SessionContext;

/**
 * 取消认领任务
 * @author DougLei
 */
public class UnclaimTaskCmd implements Command {
	private TaskEntity entity;
	private String userId; // 要取消认领的用户id
	private boolean strict; // 如果进行过抄送操作, 且抄送被查看, 取消认领时是否强制删除相关数据; 建议传入false
	public UnclaimTaskCmd(TaskEntity entity, String userId, boolean strict) {
		this.entity = entity;
		this.userId = userId;
		this.strict = strict;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(!entity.isUserTask())
			throw new TaskHandleException("取消认领失败, ["+entity.getName()+"]任务不支持用户取消认领");
		
		// 查询指定userId, 判断其是否可以取消认领
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
						Arrays.asList(entity.getTask().getTaskinstId(), userId, HandleState.CLAIMED.name()));
		if(assigneeList.isEmpty())
			throw new TaskHandleException("取消认领失败, 指定的userId无法取消认领["+entity.getName()+"]任务");
		
		// 处理抄送信息
		if(ccBeViewed()) {
			if(!strict)
				return new ExecutionResult("取消认领失败, 指定的userId抄送了[%s]任务, 且已被相关人员查看, 如确实需要取消认领, 请联系流程管理员", "jbpm.unclaim.fail.cc.be.viewed", entity.getName());
			SessionContext.getSqlSession().executeUpdate("delete bpm_hi_cc where taskinst_id=? and cc_user_id=?", Arrays.asList(entity.getTask().getTaskinstId(), userId));
		}
		if(existsCC)
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_cc where taskinst_id=? and cc_user_id=?", Arrays.asList(entity.getTask().getTaskinstId(), userId));
		
		// 取消认领, 并将比自己小的HandleState值从INVALID_UNCLAIM恢复为COMPETITIVE_UNCLAIM
		SessionContext.getSQLSession().executeUpdate("Assignee", "unclaimTask", assigneeList);
		assigneeList.stream().filter(assignee -> !assignee.isChainFirst()).forEach(assignee -> {
			SessionContext.getSqlSession().executeUpdate(
					"update bpm_ru_assignee set handle_state=? where taskinst_id=? and group_id=? and chain_id <? and handle_state=?", 
					Arrays.asList(HandleState.COMPETITIVE_UNCLAIM.name(), entity.getTask().getTaskinstId(), assignee.getGroupId(), assignee.getChainId(), HandleState.INVALID_UNCLAIM.name()));
		});
		
		// 处理task的isAllClaimed字段值, 改为没有全部认领
		if(entity.getTask().isAllClaimed())
			entity.getTask().setNotAllClaimed();
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	private boolean existsCC; // 是否有抄送
	private boolean ccBeViewed() { // 抄送是否被查看
		Option option = ((UserTaskMetadata)entity.getTaskMetadataEntity().getTaskMetadata()).getOption(OptionTypeConstants.CARBON_COPY);
		if(option == null)
			return false;
		
		List<Object> params = Arrays.asList(entity.getTask().getTaskinstId());
		
		int ccCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_cc where taskinst_id=? and cc_user_id=?", params)[0].toString());
		this.existsCC = ccCount > 0;
		
		int beViewedCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_hi_cc where taskinst_id=? and cc_user_id=?", params)[0].toString());
		return beViewedCount > 0;
	}
}
