package com.douglei.bpm.process.executor;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.executor.flow.FlowExecutionParameter;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;
import com.douglei.bpm.process.metadata.node.TaskMetadata;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class Executor<M extends ProcessNodeMetadata, EM extends ExecutionParameter> {
	
	@Autowired
	protected Executors executors;
	
	/**
	 * 执行flow
	 * @param metadata
	 * @param executionParameter
	 * @return 
	 */
	protected final boolean executeFlow(TaskMetadata metadata, FlowExecutionParameter executionParameter) {
		for(FlowMetadata flow : metadata.getFlows()) {
			if(executors.execute(flow, executionParameter).isSuccess())
				return true;
		}
		return false;
	}
	
	/**
	 * 执行
	 * @param metadata
	 * @param parameter
	 * @return 
	 */
	public abstract ExecutionResult<?> execute(M metadata, EM parameter);
	
	/**
	 * 获取执行器类型
	 * @return
	 */
	protected abstract NodeType getType();
}
