package com.douglei.bpm.process.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@Bean
@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcessHandlers implements CustomAutowired{
	private Map<Type, TaskHandler> taskHandlerMap = new HashMap<Type, TaskHandler>();
	
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<TaskHandler>)beanContainer.get(TaskHandler.class)).forEach(handler -> {
			taskHandlerMap.put(handler.getType(), handler);
		});
	}
	
	/**
	 * 启动任务
	 * @param task
	 * @param executeParameter
	 * @return
	 */
	public ExecutionResult startup(TaskMetadata task, ExecuteParameter executeParameter) {
		return taskHandlerMap.get(task.getType()).startup(task, executeParameter);
	}
	
	/**
	 * 执行任务
	 * @param task
	 * @param executeParameter
	 * @return
	 */
	public ExecutionResult execute(TaskMetadata task, ExecuteParameter executeParameter) {
		return taskHandlerMap.get(task.getType()).execute(task, executeParameter);
	}
}
