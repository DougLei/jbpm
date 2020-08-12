package com.douglei.bpm;

import com.douglei.bpm.module.history.HistoryModule;
import com.douglei.bpm.module.repository.RepositoryModule;
import com.douglei.bpm.module.runtime.RuntimeModule;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 流程引擎
 * @author DougLei
 */
public final class ProcessEngine {
	private String id;
	private RepositoryModule repository;
	private RuntimeModule runtime;
	private HistoryModule history;
	
	ProcessEngine(String id) {
		this.id = id;
	}
	
	void setRepository(RepositoryModule repository) {
		this.repository = repository;
	}
	void setRuntime(RuntimeModule runtime) {
		this.runtime = runtime;
	}
	void setHistory(HistoryModule history) {
		this.history = history;
	}

	/**
	 * 获取引擎唯一标识, 该标识引用{@link SessionFactory} 的id
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 获取Repository模块
	 * @return
	 */
	public RepositoryModule getRepository() {
		return repository;
	}
	
	/**
	 * 获取Runtime模块
	 * @return
	 */
	public RuntimeModule getRuntime() {
		return runtime;
	}
	
	/**
	 * 获取History模块
	 * @return
	 */
	public HistoryModule getHistory() {
		return history;
	}
}
