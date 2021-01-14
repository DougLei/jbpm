package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.process.api.user.option.impl.CarbonCopyOption;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.option.OptionEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 取消认领任务
 * @author DougLei
 */
public class UnclaimTaskCmd implements Command {
	private TaskInstance taskInstance;
	private String userId; // 要取消认领的用户id
	public UnclaimTaskCmd(TaskInstance taskInstance, String userId) {
		this.taskInstance = taskInstance;
		this.userId = userId;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(!taskInstance.requiredUserHandle())
			return new ExecutionResult("取消认领失败, ["+taskInstance.getName()+"]任务不支持用户取消认领");
		
		// 查询指定userId, 判断其是否可以取消认领
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), userId, HandleState.CLAIMED.name()));
		if(assigneeList.isEmpty())
			return new ExecutionResult("取消认领失败, 指定的userId无法取消认领["+taskInstance.getName()+"]任务");
		
		ccBeViewed();
		if(ccBeViewed)
			return new ExecutionResult("取消认领失败, 指定的userId抄送了["+taskInstance.getName()+"]任务, 且已被相关人员阅读, 如确实需要取消认领, 请联系流程管理员");
		
		// 取消认领
		SessionContext.getSQLSession().executeUpdate("Assignee", "unclaimTask", assigneeList);

		// 处理抄送信息
		if(existsCC)
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_cc where taskinst_id=? and cc_user_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId(), userId));
		
		// 处理task的isAllClaimed字段值, 改为没有全部认领
		if(taskInstance.getTask().isAllClaimed())
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_all_claimed=0 where taskinst_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId()));
		return null;
	}
	
	private boolean existsCC; // 是否有抄送
	private boolean ccBeViewed; // 抄送是否被查看
	private void ccBeViewed() { // 抄送是否被查看
		OptionEntity carbonCopyOptionEntity = ((UserTaskMetadata)taskInstance.getTaskMetadataEntity().getTaskMetadata()).getOptionEntity(CarbonCopyOption.TYPE);
		if(carbonCopyOptionEntity == null)
			return;
		
		// TODO 应该再判断下抄送是否是动态的, 如果是动态的, 也就不用去查询了
		
		this.existsCC = true;
		
		int beViewedCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_hi_cc where taskinst_id=? and cc_user_id=?", Arrays.asList(taskInstance.getTask().getTaskinstId()))[0].toString());
		this.ccBeViewed = beViewedCount > 0;
	}
}
