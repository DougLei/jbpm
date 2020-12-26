package com.douglei.bpm.process.handler.task.user;

import java.util.Arrays;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.task.user.assignee.AssignedUserHandler;
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
		
		Task task = new Task(handleParameter.getProcessEntity().getProcessMetadata().getId(), handleParameter.getProcessEntity().getProcinstId(), taskMetadata);
		SessionContext.getTableSession().save(task);
		
		SessionContext.getTableSession().save(
				new AssignedUserHandler(handleParameter.getProcessEntity().getProcessMetadata().getCode(), 
						handleParameter.getProcessEntity().getProcessMetadata().getVersion(), 
						handleParameter.getUserEntity().getAssignedUsers())
				.getAssigneeList(task.getTaskinstId()));
		return new ExecutionResult(task);
	}

	@Override
	public ExecutionResult handle() {
		// 处理当前办理任务的用户信息: 将办理状态改为完成
		SessionContext.getSqlSession().executeUpdate(
				"update bpm_ru_assignee set handle_state=?, attitude=?, suggest=?, finish_time=? where taskinst_id=? and user_id=? and handle_state=?", 
				Arrays.asList(HandleState.FINISHED.name(),
						handleParameter.getUserEntity().getAttitude(), 
						handleParameter.getUserEntity().getSuggest(),
						handleParameter.getCurrentDate(), 
						handleParameter.getTaskInstance().getTaskinstId(), 
						handleParameter.getUserEntity().getHandledUser().getUserId(),
						HandleState.CLAIMED.name()));
		
		completeTask();
		
		beanInstances.getTaskHandlerUtil().dispatch(taskMetadata, handleParameter);
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
