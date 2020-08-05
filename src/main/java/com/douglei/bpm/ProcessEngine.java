package com.douglei.bpm;

import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 流程引擎
 * @author DougLei
 */
public class ProcessEngine {
	private SessionFactory sf;
	
	ProcessEngine(SessionFactory sf){
		this.sf = sf;
	}
	
	/**
	 * 获取引擎唯一标识, 该标识引用{@link SessionFactory#getId()}
	 * @return
	 */
	public String getId() {
		return sf.getId();
	}
	
	
	// 获取RE的操作
	// 获取RU的操作
	// 获取HI的操作
	
}
