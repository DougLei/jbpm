package com.douglei.bpm.process.handler.task.user;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.AbstractTaskHandler;
import com.douglei.bpm.process.handler.GeneralExecuteParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.components.assignee.AssigneeHandler;
import com.douglei.bpm.process.handler.components.scheduler.TaskDispatchParameter;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=TaskHandler.class)
public class UserTaskHandler extends AbstractTaskHandler implements TaskHandler<UserTaskMetadata, TaskDispatchParameter, GeneralExecuteParameter> {

	@Override
	public ExecutionResult startup(UserTaskMetadata userTask, TaskDispatchParameter parameter) {
		Task task = new Task(parameter.getProcdefId(), parameter.getProcinstId(), userTask);
		SessionContext.getTableSession().save(task);
		
		if(!parameter.getAssigners().isEmpty()) 
			SessionContext.getTableSession().save(new AssigneeHandler(task, parameter).getAssigneeList());
		return new ExecutionResult(task);
	}

	@Override
	public ExecutionResult execute(UserTaskMetadata userTask, GeneralExecuteParameter executeParameter) {
		TaskDispatchParameter taskDispatchParameter = completeTask(executeParameter);
		taskScheduler.dispatch(userTask, taskDispatchParameter);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}
