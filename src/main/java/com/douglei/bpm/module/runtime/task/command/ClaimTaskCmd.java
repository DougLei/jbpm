package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.ProcessEngineException;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.AssigneeMode;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ClaimTaskCmd implements Command{
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
		
		// 查询指定userId, 判断其是否满足认领条件
		Assignee assignee = SessionContext.getSqlSession().uniqueQuery(Assignee.class, 
				"select id, group_id, mode, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=?", 
				Arrays.asList(taskEntity.getTask().getTaskinstId(), userId));
		if(assignee == null)
			return new ExecutionResult("认领失败, 指定的userId没有["+taskEntity.getName()+"]任务的办理权限");
		if(assignee.getHandleStateInstance().isClaimed())
			return new ExecutionResult("认领失败, 指定的userId已认领["+taskEntity.getName()+"]任务");
		
		if(assignee.getModeInstance() == AssigneeMode.ASSIST) {
			return directClaim(assignee);
		}else {
			return claim(assignee);
		}
	}

	// 直接认领(协助模式)
	private ExecutionResult directClaim(Assignee assignee) {
		SessionContext.getSqlSession().executeUpdate(
				"update bpm_ru_assignee set handle_state=?, claim_time=? where id=?", 
				Arrays.asList(HandleState.CLAIMED.name(), new Date(), assignee.getId()));
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 非协助模式下的任务认领
	private synchronized ExecutionResult claim(Assignee assignee) {
		// 查询同组内有没有人已经认领
		Object[] claimedInGroup = SessionContext.getSqlSession() 
				.uniqueQuery_("select id from bpm_ru_assignee where taskinst_id=? and group_id=? and mode <> ? and handle_state in (?,?)", 
						Arrays.asList(taskEntity.getTask().getTaskinstId(), assignee.getGroupId(), AssigneeMode.ASSIST.name(), HandleState.CLAIMED.name(), HandleState.FINISHED.name()));
		if(claimedInGroup != null)
			return new ExecutionResult("认领失败, ["+taskEntity.getName()+"]任务已被认领");
		
		// 查询当前任务一共可以有多少人认领
		List<Assignee> assigneeList = SessionContext.getSqlSession().query(Assignee.class, 
				"select handle_state from bpm_ru_assignee where taskinst_id=? and and mode <> ? handle_state <> ?", 
				Arrays.asList(taskEntity.getTask().getTaskinstId(), AssigneeMode.ASSIST.name(), HandleState.INVALID.name()));
		if(assigneeList.isEmpty())
			throw new ProcessEngineException("认领失败, id为["+taskEntity.getTask().getId()+"]的任务, 没有查询到任何有效的指派信息");
		
//		int totalCount = assigneeList.size(); // 记录的是可以认领的总人数
		int claimCount = 0; // 已经认领的数量
		for (Assignee a : assigneeList) {
			if(a.getHandleStateInstance().isClaimed())
				claimCount++;
		}
		
		// 验证认领人数是否已经达到上限
		if(claimCount == 1) // TODO 目前只是实现了单人办理模式, 所以只要有人认领就无法重复认领, 后续在这里加上多人认领的验证 
			return new ExecutionResult("认领失败, ["+taskEntity.getName()+"]任务已被认领");
		
		// 进行认领
		if(assignee.getHandleStateInstance() == HandleState.INVALID) // 将同组中非INVALID的改为INVALID状态
			SessionContext.getSqlSession().executeUpdate(
					"update bpm_ru_assignee set handle_state=? where taskinst_id=? and group_id=? and handle_state <> ?", 
					Arrays.asList(HandleState.INVALID.name(), assignee.getTaskinstId(), assignee.getGroupId(), HandleState.INVALID.name()));
		directClaim(assignee);
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
