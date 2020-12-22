package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
public class ClaimTaskCmd implements Command{
	private static final Object claimTaskKey = new Object();
	private TaskEntity taskEntity;
	private String userId; // 要认领的用户id
	
	public ClaimTaskCmd(TaskEntity taskEntity, String userId) {
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
			return new ExecutionResult("认领失败, ["+taskEntity.getName()+"]任务不支持用户处理");
		
		Assignee assignee = SessionContext.getSqlSession().uniqueQuery(Assignee.class, "select id, group_id, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=?", Arrays.asList(taskEntity.getTask().getTaskinstId(), userId));
		if(assignee == null)
			return new ExecutionResult("认领失败, 指定的userId没有["+taskEntity.getName()+"]任务的认领权限");
		if(assignee.getHandleStateInstance() == HandleState.CLAIM || assignee.getHandleStateInstance() == HandleState.FINISHED)
			return new ExecutionResult("认领失败, 指定的userId已认领["+taskEntity.getName()+"]任务");
		
		
		// 查询一共可以有多少人认领, 以及已经有多少个人进行了认领，判断是否可以继续认领
		synchronized (claimTaskKey) {
			List<Assignee> assigneeList = SessionContext.getSqlSession().query(Assignee.class, "select handle_state from bpm_ru_assignee where taskinst_id=? and handle_state <> ?", Arrays.asList(taskEntity.getTask().getTaskinstId(), HandleState.INVALID.name()));
			
			int totalCount = assigneeList.size();
			int claimCount = 0;
			if(!assigneeList.isEmpty()) {
				for (Assignee a : assigneeList) {
					if(a.getHandleStateInstance() != HandleState.UNCLAIM)
						claimCount++;
				}
			}
			
			// 验证是否可以认领
			if(totalCount == claimCount)
				return new ExecutionResult("认领失败, ["+taskEntity.getName()+"]任务已被认领");
		}
		SessionContext.getSqlSession().executeUpdate("update bpm_ru_assignee set handle_state=?, start_time=? where id=?", Arrays.asList(HandleState.CLAIM.name(), new Date(), assignee.getId()));
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
