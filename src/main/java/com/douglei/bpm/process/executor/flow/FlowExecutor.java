package com.douglei.bpm.process.executor.flow;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.executor.Executor;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Executor.class)
public class FlowExecutor extends Executor<FlowMetadata, FlowExecutionParameter>{
	private ExecutionResult<Object> success = new ExecutionResult<Object>(new Object());
	private ExecutionResult<Object> fail = new ExecutionResult<Object>(null);
	
	@Override
	public ExecutionResult<Object> execute(FlowMetadata metadata, FlowExecutionParameter parameter) {
		// 判断Flow条件是否满足
		if(metadata == null) 
			return fail;
		
		executors.execute(metadata.getTargetTask(), parameter.buildGeneralTaskExecutionParameter());
		return success;
	}
	
	@Override
	protected NodeType getType() {
		return NodeType.FLOW;
	}
}
