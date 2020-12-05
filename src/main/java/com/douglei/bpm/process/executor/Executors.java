package com.douglei.bpm.process.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
@Bean
@SuppressWarnings({"unchecked", "rawtypes"})
public class Executors implements CustomAutowired{
	private Map<NodeType, Executor> executors = new HashMap<NodeType, Executor>();
	
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<Executor>)beanContainer.get(Executor.class)).forEach(executor -> {
			executors.put(executor.getType(), executor);
		});
	}
	
	/**
	 * 
	 * @param metadata
	 * @param parameter
	 * @return
	 */
	public ExecutionResult execute(ProcessNodeMetadata metadata, ExecutionParameter parameter) {
		Executor executor = executors.get(metadata.getType());
		if(executor == null)
			throw new ExecuteException("不支持["+metadata.getClass().getName()+"]类型的执行器");
		return executor.execute(metadata, parameter);
	}
}
