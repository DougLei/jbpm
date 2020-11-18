package com.douglei.bpm;

import com.douglei.bpm.bean.Autowire;
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
	
	@Autowire
	private RepositoryModule repositoryModule;
	
	@Autowire
	private RuntimeModule runtimeModule;
	
	@Autowire
	private HistoryModule historyModule;
	
	protected ProcessEngine(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public RepositoryModule getRepositoryModule() {
		return repositoryModule;
	}
	public RuntimeModule getRuntimeModule() {
		return runtimeModule;
	}
	public HistoryModule getHistoryModule() {
		return historyModule;
	}
	
	/**
	 * 销毁引擎
	 * @throws MappingHandlerException
	 */
	protected abstract void destroy() throws MappingHandlerException;
}
