package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.CommandExecuteException;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.AssignMode;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ClaimTaskCmd implements Command{
	private TaskInstance taskInstance;
	private String userId; // 要认领的用户id
	private ClaimTaskParameter claimTaskParameter;
	public ClaimTaskCmd(TaskInstance taskInstance, String userId) {
		this.taskInstance = taskInstance;
		this.userId = userId;
	}
	
	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(!taskInstance.requiredUserHandle())
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务不支持用户处理");
		
		// 查询指定userId, 判断其是否满足认领条件
		List<Assignee> assigneeList = SessionContext.getSqlSession()
				.query(Assignee.class, 
						"select id, group_id, mode, handle_state from bpm_ru_assignee where taskinst_id=? and user_id=?", 
						Arrays.asList(taskInstance.getTask().getTaskinstId(), userId));
		if(assigneeList.isEmpty())
			return new ExecutionResult("认领失败, 指定的userId没有["+taskInstance.getName()+"]任务的办理权限");
		
		for (Assignee assignee : assigneeList) {
			if(assignee.getHandleStateInstance().isClaimed())
				return new ExecutionResult("认领失败, 指定的userId已认领["+taskInstance.getName()+"]任务");
		}
		
		claimTaskParameter = new ClaimTaskParameter(assigneeList.size(), taskInstance.getTask().getTaskinstId());
		for(int i=0;i<assigneeList.size();i++) {
			if(assigneeList.get(i).getModeInstance() == AssignMode.ASSISTED) 
				claimTaskParameter.addAssigneeId(assigneeList.remove(i--).getId());
		}
		directClaim();
		
		if(!assigneeList.isEmpty())
			return claim(assigneeList);
		return ExecutionResult.getDefaultSuccessInstance();
	}

	// 直接认领
	private void directClaim() {
		if(claimTaskParameter.getAssigneeIds().isEmpty())
			return;
		SessionContext.getSQLSession().executeUpdate("Assignee", "claimTask", claimTaskParameter);
		claimTaskParameter.getAssigneeIds().clear();;
	}
	
	// 任务认领
	private synchronized ExecutionResult claim(List<Assignee> assigneeList) {
		// 查询同组内有没有人已经认领
		for(int i=0;i<assigneeList.size();i++) {
			if(SessionContext.getSqlSession().uniqueQuery_(
					"select id from bpm_ru_assignee where taskinst_id=? and group_id=? and mode <> ? and handle_state in (?,?)", 
					Arrays.asList(taskInstance.getTask().getTaskinstId(), 
							assigneeList.get(i).getGroupId(), 
							AssignMode.ASSISTED.name(), 
							HandleState.CLAIMED.name(), 
							HandleState.FINISHED.name())) != null)
				assigneeList.remove(i--);
		}
		if(assigneeList.isEmpty())
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务已被认领");
		
		// 查询当前任务一共可以有多少人认领
		List<Assignee> supportAssigneeList = SessionContext.getSqlSession().query(Assignee.class, 
				"select handle_state from bpm_ru_assignee where taskinst_id=? and mode <> ? and handle_state <> ?", 
				Arrays.asList(taskInstance.getTask().getTaskinstId(), AssignMode.ASSISTED.name(), HandleState.INVALID.name()));
		if(supportAssigneeList.isEmpty())
			throw new CommandExecuteException("认领异常, id为["+taskInstance.getTask().getId()+"]的任务, 没有查询到任何有效的指派信息");
		
//		int totalCount = assigneeList.size(); // 记录的是可以认领的总人数
		int claimCount = 0; // 已经认领的数量
		for (Assignee sa : supportAssigneeList) {
			if(sa.getHandleStateInstance().isClaimed())
				claimCount++;
		}
		
		// 验证认领人数是否已经达到上限
		if(claimCount == 1) // TODO 目前只是实现了单人办理模式, 所以只要有人认领就无法重复认领, 后续在这里加上多人认领的验证 ; 后续判断时要注意, 如果一个人替多个人办理, 则这里判断数量时, 随机取部分认领即可
			return new ExecutionResult("认领失败, ["+taskInstance.getName()+"]任务已被认领");
		
		// 进行认领
		for (Assignee assignee : assigneeList) {
			if(assignee.getHandleStateInstance() == HandleState.INVALID) // 将同组中非INVALID的改为INVALID状态
				claimTaskParameter.addGroupId(assignee.getGroupId());
			claimTaskParameter.addAssigneeId(assignee.getId());
		}
		
		if(claimTaskParameter.getGroupIds() != null)
			SessionContext.getSQLSession().executeUpdate("Assignee", "handleState2Invalid", claimTaskParameter);
		directClaim();
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
