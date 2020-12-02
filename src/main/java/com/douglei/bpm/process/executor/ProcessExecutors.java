package com.douglei.bpm.process.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.component.ExecutionResult;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
@Bean
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessExecutors implements CustomAutowired{
	private Map<Class, ProcessExecutor> executors = new HashMap<Class, ProcessExecutor>();
	
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<ProcessExecutor>)beanContainer.get(ProcessExecutor.class)).forEach(executor -> {
			executors.put(executor.getMetadataClass(), executor);
		});
	}
	
	/**
	 * 
	 * @param metadata
	 * @param parameter
	 * @return
	 */
	public ExecutionResult execute(ProcessNodeMetadata metadata, ProcessExecutionParameter parameter) {
		ProcessExecutor executor = executors.get(metadata.getClass());
		if(executor == null)
			throw new ProcessExecuteException("不支持["+metadata.getClass().getName()+"]类型的执行器");
		return executor.execute(metadata, parameter);
	}
}
