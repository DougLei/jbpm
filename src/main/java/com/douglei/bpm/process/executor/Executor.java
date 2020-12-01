package com.douglei.bpm.process.executor;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.process.metadata.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class Executor<M extends ProcessNodeMetadata, EM extends ExecutionParameter> {

	/**
	 * 获取元数据的类型
	 * @return
	 */
	public abstract Class<M> getMetadataClass();
	
	/**
	 * 执行
	 * @param metadata
	 * @param parameter
	 * @return 
	 */
	public abstract void execute(M metadata, EM parameter);
}
