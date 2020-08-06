package com.douglei.bpm;

import com.douglei.bpm.component.hi.HistoryComponent;
import com.douglei.bpm.component.re.RepositoryComponent;
import com.douglei.bpm.component.ru.RuntimeComponent;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 流程引擎
 * @author DougLei
 */
public class ProcessEngine {
	private final String id; // 引擎唯一标识
	private final RepositoryComponent repository;
	private final RuntimeComponent runtime;
	private final HistoryComponent history;
	
	ProcessEngine(SessionFactory sessionFactory){
		this.id = sessionFactory.getId();
		this.repository = new RepositoryComponent();
		this.runtime = new RuntimeComponent();
		this.history = new HistoryComponent();
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
	public RepositoryComponent getRepository() {
		return repository;
	}
	
	/**
	 * 获取Runtime组件
	 * @return
	 */
	public RuntimeComponent getRuntime() {
		return runtime;
	}
	
	/**
	 * 获取History组件
	 * @return
	 */
	public HistoryComponent getHistory() {
		return history;
	}
}
