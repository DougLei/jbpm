package com.douglei.bpm.process.handler.task.user;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.task.assignee.Assigners;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.AbstractTaskHandler;
import com.douglei.bpm.process.handler.GeneralExecuteParameter;
import com.douglei.bpm.process.handler.TaskDispatchParameter;
import com.douglei.bpm.process.handler.TaskHandler;
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
		
		/* 
		 * TODO 这里目前缺少的是对指派人员的处理
		 * 
		 * 有两个来源:
		 * 1.用户在上一环节选的
		 * 2.当前任务环节配置的固定指派人员
		 * 
		 * 
		 * 同理就会涉及到委托表的配置信息
		 * 
		 */
		
		Assigners assigners = parameter.getAssigners();
		if(!assigners.isEmpty()) 
			SessionContext.getTableSession().save(assigners.getAssigneeList(task.getId()));
		
		return new ExecutionResult(task);
	}

	@Override
	public ExecutionResult execute(UserTaskMetadata userTask, GeneralExecuteParameter parameter) {
		TaskDispatchParameter taskDispatchParameter = completeTask(parameter.getTaskInstance());
		taskScheduler.dispatch(userTask, taskDispatchParameter);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}
