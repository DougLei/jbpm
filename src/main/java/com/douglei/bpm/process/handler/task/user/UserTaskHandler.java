package com.douglei.bpm.process.handler.task.user;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryAssignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.task.user.assignee.AssignedUserHandler4Handling;
import com.douglei.bpm.process.handler.task.user.assignee.AssignedUserHandler4Startup;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class UserTaskHandler extends TaskHandler<UserTaskMetadata, GeneralHandleParameter> {

	// TODO 这里增加一个处理指派人的方法
	
	@Override
	public ExecutionResult startup() {
		if(handleParameter.getUserEntity().getAssignedUsers().isEmpty())
			throw new TaskHandleException("id为["+taskMetadata.getId()+"], name为["+taskMetadata.getName()+"]的任务没有指派具体的办理人员");
		
		Task task = createTask(true);
		
		new AssignedUserHandler4Startup(handleParameter.getProcessEntity().getProcessMetadata().getCode(), 
				handleParameter.getProcessEntity().getProcessMetadata().getVersion(), 
				handleParameter.getUserEntity().getAssignedUsers()).saveAssigneeList(task.getTaskinstId());
		return new ExecutionResult(task);
	}

	@Override
	public ExecutionResult handle() {
		assigneeDispatch();
		completeTask();
		
		beanInstances.getTaskHandlerUtil().dispatch(taskMetadata, handleParameter);
		return ExecutionResult.getDefaultSuccessInstance();
	}

	// 指派信息调度
	private void assigneeDispatch() {
		List<HistoryAssignee> assigneeList = SessionContext.getSqlSession().query(HistoryAssignee.class, "select * from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
				Arrays.asList(handleParameter.getTask().getTaskinstId(),
						handleParameter.getUserEntity().getHandledUser().getUserId(),
						HandleState.CLAIMED.name()));
		new AssignedUserHandler4Handling(handleParameter, assigneeList).assigneeDispatch();
	}
}
