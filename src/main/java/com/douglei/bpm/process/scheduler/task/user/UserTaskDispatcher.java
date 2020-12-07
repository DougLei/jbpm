package com.douglei.bpm.process.scheduler.task.user;

import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.entity.Assignee;
import com.douglei.bpm.module.runtime.task.entity.Task;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;
import com.douglei.bpm.process.scheduler.TaskDispatcher;
import com.douglei.bpm.process.scheduler.GeneralDispatchParameter;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=TaskDispatcher.class)
public class UserTaskDispatcher extends TaskDispatcher<UserTaskMetadata, GeneralDispatchParameter> {

	@Override
	public ExecutionResult<Task> dispatch(UserTaskMetadata userTask, GeneralDispatchParameter parameter) {
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
	protected Type getType() {
		return Type.USER_TASK;
	}
}
