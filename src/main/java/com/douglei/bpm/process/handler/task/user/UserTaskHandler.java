package com.douglei.bpm.process.handler.task.user;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.task.command.CarbonCopyTaskCmd;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.option.impl.carboncopy.CarbonCopyOptionHandler;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.task.user.assignee.handle.AssigneeDispatcher;
import com.douglei.bpm.process.handler.task.user.assignee.startup.AssigneeHandler;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;
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
		Task currentTask = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		
		// 进行指派信息的调度
		AssigneeDispatcher assigneeDispatcher = new AssigneeDispatcher(
				currentTask.getTaskinstId(),
				handleParameter.getUserEntity().getCurrentHandleUser().getUserId(),
				handleParameter.getUserEntity().getSuggest(), 
				handleParameter.getUserEntity().getAttitude(), 
				handleParameter.getCurrentDate());
		assigneeDispatcher.dispatch();
		
		if(isFinished()) {
			if(currentTask.getAssignCount() > 1)
				currentTask.setDispatchRight(handleParameter.getUserEntity().getCurrentHandleUser().getUserId());
			return CAN_DISPATCH;
		}
		return CANNOT_DISPATCH;
	}
	
	// 判断任务是否结束
	private boolean isFinished() {
		if(!handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().isAllClaimed())
			return false;
		
		// 查询当前任务, 已经认领的指派信息数量
		int claimedAssigneeCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_assignee where taskinst_id=? and handle_state=?", 
				Arrays.asList(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(), HandleState.CLAIMED.name()))[0].toString());
		return claimedAssigneeCount == 0;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 结束用户任务
	private void finishUserTask() {
		completeTask(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask(), handleParameter.getCurrentDate());
		followTaskCompleted4Variable(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask());
		clearAssigneeList();
		executeCarbonCopy();
		processEngineBeans.getTaskHandleUtil().dispatch(currentTaskMetadataEntity, handleParameter);
	}
	
	// 清空当前任务的所有指派信息
	private void clearAssigneeList() {
		SessionContext.getSqlSession().executeUpdate(
				"delete bpm_ru_assignee where taskinst_id=?", 
				Arrays.asList(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId()));
	}

	// 进行抄送
	private void executeCarbonCopy() {
		Option option = currentTaskMetadataEntity.getTaskMetadata().getOption(CarbonCopyOptionHandler.TYPE);
		if(option == null || ((CarbonCopyOption)option).getCandidate().getAssignPolicy().isDynamic())
			return;
		
		// 获取具体可抄送的所有人员集合, 并进行抄送
		List<UserBean> assignableUsers = processEngineBeans.getTaskHandleUtil().getAssignableUsers(
				((CarbonCopyOption)option).getCandidate().getAssignPolicy(), 
				currentTaskMetadataEntity.getTaskMetadata(), 
				handleParameter);
		
		if(assignableUsers.isEmpty())
			return;
		
		new CarbonCopyTaskCmd().execute(
				handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(), 
				handleParameter.getUserEntity().getCurrentHandleUser().getUserId(), 
				handleParameter.getCurrentDate(), 
				assignableUsers);
	}
}
