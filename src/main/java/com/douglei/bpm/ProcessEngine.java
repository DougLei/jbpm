package com.douglei.bpm;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.bpm.module.runtime.RuntimeModule;
import com.douglei.bpm.process.ProcessHandler;
import com.douglei.orm.mapping.handler.MappingHandlerException;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 流程引擎
 * @author DougLei
 */
public abstract class ProcessEngine {
	protected final SessionFactory sessionFactory; // 引擎id, 即SessionFactory的id, 所以也可以从SessionFactoryContainer.get().getId()来获取
	
	@Autowired
	private ProcessHandler processHandler;
	
	@Autowired
	private RepositoryModule repositoryModule;
	
	@Autowired
	private RuntimeModule runtimeModule;
	
	@Autowired
	private HistoryModule historyModule;
	
	protected ProcessEngine(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public final String getId() {
		return sessionFactory.getId();
	}
	public final SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public final ProcessHandler getProcessHandler() {
		return processHandler;
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
