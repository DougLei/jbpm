package com.douglei.bpm.process.handler.task.user;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryAssignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.task.user.assignee.AssignedUserHandler4TaskHandle;
import com.douglei.bpm.process.handler.task.user.assignee.AssignedUserHandler4TaskStartup;
import com.douglei.bpm.process.handler.task.user.assignee.AssignedUserValidator;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class UserTaskHandler extends TaskHandler<UserTaskMetadata, GeneralHandleParameter> {

	@Override
	public ExecutionResult startup() {
		// 验证指派的用户
		new AssignedUserValidator(currentTaskMetadataEntity.getTaskMetadata(), handleParameter).validate();
		
		// 启动当前用户任务
		Task task = createTask(false);
		if(currentTaskMetadataEntity.getTaskMetadata().getTimeLimit() != null)
			task.setExpiryTime(new TimeLimitParser(task.getStartTime(), currentTaskMetadataEntity.getTaskMetadata().getTimeLimit()).getExpiryTime());
		SessionContext.getTableSession().save(task);
		
		// 记录指派的用户
		new AssignedUserHandler4TaskStartup(handleParameter.getProcessMetadata().getCode(), 
				handleParameter.getProcessMetadata().getVersion(), 
				handleParameter.getUserEntity().getAssignedUsers()).saveAssigneeList(task.getTaskinstId());
		return new ExecutionResult(task);
	}

	@Override
	public ExecutionResult handle() {
		assigneeDispatch();
		
		if(isFinished()) 
			finishUserTask();
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 指派信息调度
	private void assigneeDispatch() {
		List<HistoryAssignee> assigneeList = SessionContext.getSqlSession().query(HistoryAssignee.class, "select * from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
				Arrays.asList(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(),
						handleParameter.getUserEntity().getCurrentHandleUser().getUserId(),
						HandleState.CLAIMED.name()));
		new AssignedUserHandler4TaskHandle(handleParameter, assigneeList).assigneeDispatch();
	}

	// 判断任务是否结束
	private boolean isFinished() {
		// TODO 多人办理时的完成策略
		return true;
	}

	// 结束用户任务
	private void finishUserTask() {
		completeTask(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask(), handleParameter.getCurrentDate());
		followTaskCompleted4Variable(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask());
		processEngineBeans.getTaskHandleUtil().dispatch(currentTaskMetadataEntity, handleParameter);
	}
}
