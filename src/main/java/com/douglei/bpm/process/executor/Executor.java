package com.douglei.bpm.process.executor;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class Executor<M extends ProcessNodeMetadata, EM extends ExecutionParameter> {
	
	@Autowired
	protected Executors executors;
	
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
