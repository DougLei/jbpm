package com.douglei.bpm.process.executor.flow;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.component.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.executor.Executor;
import com.douglei.bpm.process.executor.ProcessExecutionParameter;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Executor.class)
public class FlowExecutor extends Executor<FlowMetadata, ProcessExecutionParameter>{
	private ExecutionResult<Object> success = new ExecutionResult<Object>(new Object());
	private ExecutionResult<Object> fail = new ExecutionResult<Object>(null);
	
	@Override
	public ExecutionResult<Object> execute(FlowMetadata metadata, ProcessExecutionParameter parameter) {
		// TODO Auto-generated method stub
		
		/*
		 * 1. 判断条件是否满足
		 * 2. 如果满足则找到下一个任务并执行, 以及创建执行参数并传入
		 * 
		 */
		
		
		
		if(metadata == null) // 条件不满足
			return fail;
		
		executors.execute(metadata.getTargetTask(), null);
		return success;
	}
	
	@Override
	protected NodeType getType() {
		return NodeType.FLOW;
	}
}
