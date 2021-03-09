package com.douglei.bpm.process.handler.task.user;

import java.util.Arrays;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.task.user.assignee.handle.AssigneeDispatcher;
import com.douglei.bpm.process.handler.task.user.assignee.startup.AssigneeHandler;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class UserTaskHandler extends TaskHandler<UserTaskMetadata, GeneralHandleParameter> {
	private final static ExecutionResult CAN_DISPATCH = new ExecutionResult(true);

	@Override
	public ExecutionResult startup() {
		// 验证指派的用户
		AssigneeHandler assigneeHandler = new AssigneeHandler(
				handleParameter.getProcessMetadata().getCode(), 
				handleParameter.getProcessMetadata().getVersion(), 
				handleParameter.getUserEntity().getAssignedUsers());
		assigneeHandler.pretreatment(currentTaskMetadataEntity.getTaskMetadata(), handleParameter, processEngineBeans);
				
		// 创建当前用户任务
		Task task = createTask(false);
		task.setAssignCount(assigneeHandler.getAssignCount());
		if(currentTaskMetadataEntity.getTaskMetadata().getTimeLimit() != null)
			task.setExpiryTime(new TimeLimitParser(task.getStartTime(), currentTaskMetadataEntity.getTaskMetadata().getTimeLimit()).getExpiryTime());
		
		// 记录指派的用户
		assigneeHandler.save(
				handleParameter.getUserEntity().getCurrentHandleUser().getUserId(), 
				currentTaskMetadataEntity.getTaskMetadata(),
				task,
				handleParameter.getCurrentDate());
		
		// 保存当前用户任务
		SessionContext.getTableSession().save(task);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public ExecutionResult handle() {
		// 更新businessId
		Task currentTask = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		currentTask.addBusinessId(handleParameter.getBusinessId());
		
		// 进行指派信息的调度
		AssigneeDispatcher assigneeDispatcher = new AssigneeDispatcher(
				currentTask.getTaskinstId(),
				handleParameter.getUserEntity().getCurrentHandleUser().getUserId(),
				handleParameter.getUserEntity().getSuggest(), 
				handleParameter.getUserEntity().getAttitude(), 
				handleParameter.getCurrentDate());
		assigneeDispatcher.dispatch();
		
		// 判断任务是否结束, 以及是否可以调度
		if(isFinished()) {
			currentTask.setDispatchRight(handleParameter.getUserEntity().getCurrentHandleUser().getUserId());
			return CAN_DISPATCH;
		}
		return CANNOT_DISPATCH;
	}
	
	// 判断任务是否结束
	private boolean isFinished() {
		Task currentTask = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		if(!currentTask.isAllClaimed())
			return false;
		
		if(currentTask.getAssignCount() == 1)
			return true;
		
		// 查询当前任务, 已经认领的指派信息数量
		int claimedAssigneeCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_assignee where taskinst_id=? and handle_state=?", 
				Arrays.asList(currentTask.getTaskinstId(), HandleState.CLAIMED.name()))[0].toString());
		return claimedAssigneeCount == 0;
	}
}
