package com.douglei.bpm;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.bpm.module.runtime.RuntimeModule;
import com.douglei.orm.mapping.handler.MappingHandlerException;

/**
 * 流程引擎
 * @author DougLei
 */
public abstract class ProcessEngine {
	protected String id; // 引擎id, 即SessionFactory的id, 所以也可以从SessionFactoryContainer.get().getId()来获取
	
	@Attribute
	private RepositoryModule repository;
	
	@Attribute
	private RuntimeModule runtime;
	
	@Attribute
	private HistoryModule history;
	
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
	
	/**
	 * 销毁引擎
	 * @throws MappingHandlerException
	 */
	protected abstract void destroy() throws MappingHandlerException;
}
