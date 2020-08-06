package com.douglei.bpm;

import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 流程引擎
 * @author DougLei
 */
public class ProcessEngine {
	private String id; // 引擎唯一标识
	private RepositoryModule repository;
	private RuntimeModule runtime;
	private HistoryModule history;
	
	ProcessEngine(SessionFactory sessionFactory){
		this.id = sessionFactory.getId();
		SessionSource sessionSource = new SessionSource(sessionFactory);
		this.repository = new RepositoryModule();
		this.runtime = new RuntimeModule();
		this.history = new HistoryModule();
	}
	
	/**
	 * 获取引擎唯一标识, 该标识引用{@link SessionFactory#getId()}
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 获取Repository组件
	 * @return
	 */
	public RepositoryModule getRepository() {
		return repository;
	}
	
	/**
	 * 获取Runtime组件
	 * @return
	 */
	public RuntimeModule getRuntime() {
		return runtime;
	}
	
	/**
	 * 获取History组件
	 * @return
	 */
	public HistoryModule getHistory() {
		return history;
	}
}
