package com.douglei.bpm.process.mapping.metadata.task.user.option;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
public abstract class Option implements Serializable{
	private String type;
	private String name;
	private int order;
	
	protected Option(String type, String name, int order) {
		this.type = type;
		this.name = name;
		this.order = order;
	}
	
	/**
	 * 获取option的类型
	 * @return
	 */
	public final String getType() {
		return type;
	}
	/**
	 * 获取option的名字
	 * @return
	 */
	public final String getName() {
		return name;
	}
	/**
	 * 获取option的排序值
	 * @return
	 */
	public final int getOrder() {
		return order;
	}
	/**
	 * 是否支持指定的ActiveTime
	 * @param target
	 * @return
	 */
	public abstract boolean supportActiveTime(ActiveTime target);
}
