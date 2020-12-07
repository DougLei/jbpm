package com.douglei.bpm.process.executor;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class Executor<M extends TaskMetadata, EM extends ExecutionParameter> {
	
	@Autowired
	protected Executors executors;
	
	/**
	 * 执行
	 * @param task
	 * @param parameter
	 * @return 
	 */
	public abstract ExecutionResult<?> execute(M task, EM parameter);
	
	/**
	 * 获取执行器类型
	 * @return
	 */
	protected abstract NodeType getType();
}
