package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;

import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class UnclaimTaskCmd implements Command {
	private TaskEntity taskEntity;
	private String userId; // 要认领的用户id
	
	public UnclaimTaskCmd(TaskEntity taskEntity, String userId) {
		this.taskEntity = taskEntity;
		this.userId = userId;
	}

	@Override
	public boolean autowiredRequired() {
		return false;
	}

	@Override
	public ExecutionResult execute() {
		if(!taskEntity.supportUserHandling())
			return new ExecutionResult("放弃认领失败, ["+taskEntity.getName()+"]任务不支持用户处理");
		
		// 查询指定userId, 判断其是否满足认领条件
		Assignee assignee = SessionContext.getSqlSession().uniqueQuery(Assignee.class, 
				"select id, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=?", 
				Arrays.asList(taskEntity.getTask().getTaskinstId(), userId));
		if(assignee == null)
			return new ExecutionResult("放弃认领失败, 指定的userId没有["+taskEntity.getName()+"]任务的办理权限");
		if(assignee.getHandleStateInstance().unClaim())
			return new ExecutionResult("放弃认领失败, 指定的userId未认领["+taskEntity.getName()+"]任务");
		if(assignee.getHandleStateInstance() == HandleState.FINISHED)
			return new ExecutionResult("放弃认领失败, 指定的userId已完成["+taskEntity.getName()+"]任务的办理");
		
		// 放弃认领
		SessionContext.getSqlSession().executeUpdate(
				"update bpm_ru_assignee set handle_state=?, claim_time=null where id=?", 
				Arrays.asList(HandleState.UNCLAIM.name(), assignee.getId()));
		return null;
	}
}
