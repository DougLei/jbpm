package com.douglei.bpm.process.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.TaskMetadata;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;
import com.douglei.bpm.process.scheduler.flow.FlowDispatchParameter;
import com.douglei.bpm.process.scheduler.flow.FlowDispatcher;

/**
 * 
 * @author DougLei
 */
@Bean
@SuppressWarnings({"unchecked", "rawtypes"})
public class Dispatchers implements CustomAutowired{
	private Map<Type, TaskDispatcher> dispatchers = new HashMap<Type, TaskDispatcher>();
	
	@Autowired
	private FlowDispatcher flowDispatcher;
	
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<TaskDispatcher>)beanContainer.get(TaskDispatcher.class)).forEach(executor -> {
			dispatchers.put(executor.getType(), executor);
		});
	}
	
	/**
	 * 
	 * @param metadata
	 * @param parameter
	 * @return
	 */
	public ExecutionResult dispatchTask(TaskMetadata metadata, DispatchParameter parameter) {
		TaskDispatcher dispatcher = dispatchers.get(metadata.getType());
		if(dispatcher == null)
			throw new DispatchException("不支持["+metadata.getClass().getName()+"]类型的调度器");
		return dispatcher.dispatch(metadata, parameter);
	}

	/**
	 * 执行Flow
	 * @param metadata
	 * @param executionParameter
	 * @return 
	 */
	public boolean dispatchFlow(FlowMetadata flow, FlowDispatchParameter executionParameter) {
		return flowDispatcher.dispatch(flow, executionParameter).isSuccess();
	}
	
	/**
	 * 执行Task中的Flow
	 * @param metadata
	 * @param executionParameter
	 * @return 
	 */
	public boolean dispatchFlow(List<FlowMetadata> flows, FlowDispatchParameter executionParameter) {
		for(FlowMetadata flow : flows) {
			if(dispatchFlow(flow, executionParameter))
				return true;
		}
		return false;
	}
}
