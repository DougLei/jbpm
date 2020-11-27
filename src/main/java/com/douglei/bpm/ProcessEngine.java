package com.douglei.bpm;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.bpm.module.runtime.RuntimeModule;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.orm.mapping.handler.MappingHandlerException;

/**
 * 流程引擎
 * @author DougLei
 */
public abstract class ProcessEngine {
	protected final String id; // 引擎id, 即SessionFactory的id, 所以也可以从SessionFactoryContainer.get().getId()来获取
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	@Autowired
	private RepositoryModule repositoryModule;
	
	@Autowired
	private RuntimeModule runtimeModule;
	
	@Autowired
	private HistoryModule historyModule;
	
	protected ProcessEngine(String id) {
		this.id = id;
	}
	
	public final String getId() {
		return id;
	}
	public final ProcessContainerProxy getProcessContainer() {
		return processContainer;
	}
	public final RepositoryModule getRepositoryModule() {
		return repositoryModule;
	}
	public final RuntimeModule getRuntimeModule() {
		return runtimeModule;
	}
	public final HistoryModule getHistoryModule() {
		return historyModule;
	}
	
	/**
	 * 销毁引擎
	 * @throws MappingHandlerException
	 */
	protected abstract void destroy() throws MappingHandlerException;
}
