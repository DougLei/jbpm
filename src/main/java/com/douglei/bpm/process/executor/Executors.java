package com.douglei.bpm.process.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.executor.flow.FlowExecutionParameter;
import com.douglei.bpm.process.executor.flow.FlowExecutor;
import com.douglei.bpm.process.metadata.node.TaskMetadata;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
@Bean
@SuppressWarnings({"unchecked", "rawtypes"})
public class Executors implements CustomAutowired{
	private Map<NodeType, Executor> executors = new HashMap<NodeType, Executor>();
	
	@Autowired
	private FlowExecutor flowExecutor;
	
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<Executor>)beanContainer.get(Executor.class)).forEach(executor -> {
			executors.put(executor.getType(), executor);
		});
	}
	
	/**
	 * 任务Task
	 * @param metadata
	 * @param parameter
	 * @return
	 */
	public ExecutionResult executeTask(TaskMetadata metadata, ExecutionParameter parameter) {
		Executor executor = executors.get(metadata.getType());
		if(executor == null)
			throw new ExecuteException("不支持["+metadata.getClass().getName()+"]类型的执行器");
		return executor.execute(metadata, parameter);
	}

	/**
	 * 执行Flow
	 * @param metadata
	 * @param executionParameter
	 * @return 
	 */
	public boolean executeFlow(FlowMetadata flow, FlowExecutionParameter executionParameter) {
		return flowExecutor.execute(flow, executionParameter).isSuccess();
	}
	
	/**
	 * 执行Task中的Flow
	 * @param metadata
	 * @param executionParameter
	 * @return 
	 */
	public boolean executeFlow(List<FlowMetadata> flows, FlowExecutionParameter executionParameter) {
		for(FlowMetadata flow : flows) {
			if(executeFlow(flow, executionParameter))
				return true;
		}
		return false;
	}
}
