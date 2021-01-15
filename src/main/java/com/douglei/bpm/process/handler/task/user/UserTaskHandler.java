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

	@Override
	public ExecutionResult startup() {
		// 验证指派的用户
		AssigneeHandler assigneeHandler = new AssigneeHandler(
				handleParameter.getProcessMetadata().getCode(), 
				handleParameter.getProcessMetadata().getVersion(), 
				handleParameter.getUserEntity().getAssignedUsers());
		assigneeHandler.pretreatment(currentTaskMetadataEntity.getTaskMetadata(), handleParameter, processEngineBeans);
				
		// 启动当前用户任务
		Task task = createTask(false);
		if(currentTaskMetadataEntity.getTaskMetadata().getTimeLimit() != null)
			task.setExpiryTime(new TimeLimitParser(task.getStartTime(), currentTaskMetadataEntity.getTaskMetadata().getTimeLimit()).getExpiryTime());
		SessionContext.getTableSession().save(task);
		
		// 记录指派的用户
		assigneeHandler.save(task.getTaskinstId(), handleParameter.getUserEntity().getCurrentHandleUser().getUserId(), currentTaskMetadataEntity.getTaskMetadata());
		
		return new ExecutionResult(task);
	}

	@Override
	public ExecutionResult handle() {
		// 进行指派信息的调度
		AssigneeDispatcher assigneeDispatcher = new AssigneeDispatcher(
				handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(),
				handleParameter.getUserEntity().getCurrentHandleUser().getUserId(),
				handleParameter.getUserEntity().getSuggest(), 
				handleParameter.getUserEntity().getAttitude(), 
				handleParameter.getCurrentDate());
		assigneeDispatcher.dispatch();
		
		if(isFinished()) 
			finishUserTask(assigneeDispatcher);
		return ExecutionResult.getDefaultSuccessInstance();
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
	private void finishUserTask(AssigneeDispatcher assigneeDispatcher) {
		completeTask(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask(), handleParameter.getCurrentDate());
		followTaskCompleted4Variable(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask());
		assigneeDispatcher.dispatchAll();
		executeCarbonCopy();
		processEngineBeans.getTaskHandleUtil().dispatch(currentTaskMetadataEntity, handleParameter);
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
