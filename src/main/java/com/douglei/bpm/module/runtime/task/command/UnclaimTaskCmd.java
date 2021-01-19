package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.option.impl.carboncopy.CarbonCopyOptionHandler;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;
import com.douglei.orm.context.SessionContext;

/**
 * 取消认领任务
 * @author DougLei
 */
public class UnclaimTaskCmd implements Command {
	private TaskInstance taskInstance;
	private String userId; // 要取消认领的用户id
	private boolean strict; // 如果进行过抄送操作, 且抄送被查看, 取消认领时是否强制删除相关数据; 建议传入false
	public UnclaimTaskCmd(TaskInstance taskInstance, String userId, boolean strict) {
		this.taskInstance = taskInstance;
		this.userId = userId;
		this.strict = strict;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(!taskInstance.requiredUserHandle())
			throw new TaskHandleException("取消认领失败, ["+taskInstance.getName()+"]任务不支持用户取消认领");
		
		// 查询指定userId, 判断其是否可以取消认领
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), userId, HandleState.CLAIMED.name()));
		if(assigneeList.isEmpty())
			throw new TaskHandleException("取消认领失败, 指定的userId无法取消认领["+taskInstance.getName()+"]任务");
		
		// 处理抄送信息
		if(ccBeViewed()) {
			if(!strict)
				return new ExecutionResult("取消认领失败, 指定的userId抄送了[%s]任务, 且已被相关人员查看, 如确实需要取消认领, 请联系流程管理员", "jbpm.unclaim.fail.cc.be.viewed", taskInstance.getName());
			SessionContext.getSqlSession().executeUpdate("delete bpm_hi_cc where taskinst_id=? and cc_user_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId(), userId));
		}
		if(existsCC)
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_cc where taskinst_id=? and cc_user_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId(), userId));
		
		// 取消认领
		SessionContext.getSQLSession().executeUpdate("Assignee", "unclaimTask", assigneeList);

		// 处理task的isAllClaimed字段值, 改为没有全部认领
		if(taskInstance.getTask().isAllClaimed())
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_all_claimed=null where taskinst_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId()));
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	private boolean existsCC; // 是否有抄送
	private boolean ccBeViewed() { // 抄送是否被查看
		Option option = ((UserTaskMetadata)taskInstance.getTaskMetadataEntity().getTaskMetadata()).getOption(CarbonCopyOptionHandler.TYPE);
		if(option == null || !((CarbonCopyOption)option).getCandidate().getAssignPolicy().isDynamic())
			return false;
		
		List<Object> params = Arrays.asList(taskInstance.getTask().getTaskinstId());
		
		int ccCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_cc where taskinst_id=? and cc_user_id=?", params)[0].toString());
		this.existsCC = ccCount > 0;
		
		int beViewedCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_hi_cc where taskinst_id=? and cc_user_id=?", params)[0].toString());
		return beViewedCount > 0;
	}
}
