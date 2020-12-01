package com.douglei.bpm.process.executor;

import com.douglei.bpm.process.metadata.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractExecutor<T extends ProcessNodeMetadata> {
	protected T metadata;
	protected AbstractExecutor(T metadata) {
		this.metadata = metadata;
	}
	
	
}
