package com.douglei.bpm.process.executor.task.user;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.executor.Executor;
import com.douglei.bpm.process.executor.GeneralTaskExecutionParameter;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Executor.class)
public class UserTaskExecutor extends Executor<UserTaskMetadata, GeneralTaskExecutionParameter> {

	@Override
	public ExecutionResult<?> execute(UserTaskMetadata metadata, GeneralTaskExecutionParameter parameter) {
		// TODO 用户任务执行器
		System.out.println("进入"+metadata.getName()+"任务了");
		
		
		
		return null;
	}
	
	@Override
	protected NodeType getType() {
		return NodeType.USER_TASK;
	}
}
