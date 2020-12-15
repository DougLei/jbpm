package com.douglei.bpm.process.handler.task.user;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.task.assignee.Assigners;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.GeneralExecuteParameter;
import com.douglei.bpm.process.handler.ProcessHandlers;
import com.douglei.bpm.process.handler.TaskDispatchParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=TaskHandler.class)
public class UserTaskExecutor extends TaskHandler<UserTaskMetadata, TaskDispatchParameter, GeneralExecuteParameter>{

	@Autowired
	private ProcessHandlers handlers;
	
	@Override
	public ExecutionResult startup(UserTaskMetadata userTask, TaskDispatchParameter parameter) {
		Task task = new Task(parameter.getProcdefId(), parameter.getProcinstId(), userTask);
		SessionContext.getTableSession().save(task);
		
		Assigners assigners = parameter.getAssigners();
		if(!assigners.isEmpty()) 
			SessionContext.getTableSession().save(assigners.getAssigneeList(task.getId()));
		
		return new ExecutionResult(task);
	}

	@Override
	public ExecutionResult execute(UserTaskMetadata userTask, GeneralExecuteParameter parameter) {
		// TODO 
		return null;
	}

	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}
