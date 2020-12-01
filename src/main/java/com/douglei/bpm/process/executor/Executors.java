package com.douglei.bpm.process.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.metadata.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
@Bean
@SuppressWarnings({"unchecked", "rawtypes"})
public class Executors implements CustomAutowired{
	private Map<Class, Executor> executors = new HashMap<Class, Executor>();
	
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<Executor>)beanContainer.get(Executor.class)).forEach(executor -> {
			executors.put(executor.getMetadataClass(), executor);
		});
	}
	
	/**
	 * 
	 * @param metadata
	 * @param parameter
	 */
	public void execute(ProcessNodeMetadata metadata, ExecutionParameter parameter) {
		Executor executor = executors.get(metadata.getClass());
		if(executor == null)
			throw new ProcessExecuteException("不支持["+metadata.getClass().getName()+"]类型的执行器");
		executor.execute(metadata, parameter);
	}
}
