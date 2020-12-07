package com.douglei.bpm.process.executor.task.user;

import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.module.runtime.task.entity.Task;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.executor.Executor;
import com.douglei.bpm.process.executor.GeneralExecutionParameter;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Executor.class)
public class UserTaskExecutor extends Executor<UserTaskMetadata, GeneralExecutionParameter> {

	@Override
	public ExecutionResult<Task> execute(UserTaskMetadata userTask, GeneralExecutionParameter parameter) {
		Task task = new Task(parameter.getProcdefId(), parameter.getProcinstId(), userTask);
		SessionContext.getTableSession().save(task);
		
		List<Assignee> list = parameter.getAssignees();
		if(list != null) {
			list.forEach(assignee -> assignee.setTaskId(task.getId()));
			SessionContext.getTableSession().save(list);
		}
		return null;
	}
	
	
	
	
	
	@Override
	protected NodeType getType() {
		return NodeType.USER_TASK;
	}
}
