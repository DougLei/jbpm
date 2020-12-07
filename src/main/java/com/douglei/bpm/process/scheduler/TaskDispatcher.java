package com.douglei.bpm.process.scheduler;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class TaskDispatcher<M extends TaskMetadata, EM extends DispatchParameter> {
	
	@Autowired
	protected Dispatchers dispatchers;
	
	/**
	 * 
	 * @param task
	 * @param parameter
	 * @return 
	 */
	public abstract ExecutionResult<?> dispatch(M task, EM parameter);
	
	/**
	 * 获取调度器类型
	 * @return
	 */
	protected abstract Type getType();
}
