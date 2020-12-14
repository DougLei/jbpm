package com.douglei.bpm.process.handler.task.user;

import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.module.runtime.task.entity.Task;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.TaskDispatchParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=TaskHandler.class)
public class UserTaskExecutor extends TaskHandler<UserTaskMetadata, TaskDispatchParameter>{

	@Override
	public ExecutionResult startup(UserTaskMetadata userTask, TaskDispatchParameter parameter) {
		Task task = new Task(parameter.getProcdefId(), parameter.getProcinstId(), userTask);
		SessionContext.getTableSession().save(task);
		
		List<Assignee> list = parameter.getAssignees();
		if(list != null) {
			list.forEach(assignee -> assignee.setTaskId(task.getId()));
			SessionContext.getTableSession().save(list);
		}
		return new ExecutionResult(task);
	}

	@Override
	public ExecutionResult execute(UserTaskMetadata userTask, TaskDispatchParameter parameter) {
		// TODO 怎么解决参数问题
		return null;
	}

	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}
