package com.douglei.bpm;

import com.douglei.bpm.module.HistoryModule;
import com.douglei.bpm.module.RepositoryModule;
import com.douglei.bpm.module.RuntimeModule;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 流程引擎
 * @author DougLei
 */
@ProcessEngineBean
public class ProcessEngine {
	private String id;
	
	@ProcessEngineBeanField
	private RepositoryModule repository;
	
	@ProcessEngineBeanField
	private RuntimeModule runtime;
	
	@ProcessEngineBeanField
	private HistoryModule history;
	
	// 设置引擎的id值
	void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 获取引擎唯一标识, 该标识引用{@link SessionFactory#getId()}
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
