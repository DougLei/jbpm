package com.douglei.bpm;

import com.douglei.bpm.bean.annotation.ProcessEngineField;
import com.douglei.bpm.core.process.ProcessHandler;
import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.bpm.module.runtime.RuntimeModule;
import com.douglei.orm.core.mapping.MappingExecuteException;

/**
 * 流程引擎
 * @author DougLei
 */
public abstract class ProcessEngine {
	protected String id; // 引擎id
	
	@ProcessEngineField
	protected ProcessHandler processHandler;
	
	@ProcessEngineField
	protected RepositoryModule repository;
	
	@ProcessEngineField
	protected RuntimeModule runtime;
	
	@ProcessEngineField
	protected HistoryModule history;
	
	
	protected ProcessEngine(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public RepositoryModule getRepository() {
		return repository;
	}
	public RuntimeModule getRuntime() {
		return runtime;
	}
	public HistoryModule getHistory() {
		return history;
	}
	
	public abstract void destroy() throws MappingExecuteException;
}
