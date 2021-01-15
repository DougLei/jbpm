package com.douglei.bpm.process.metadata.task.user.option;

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
	
	@Override
	public final boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(this == obj)
			return true;
		return type.equals(((Option) obj).type);
	}

	/**
	 * 获取option的类型
	 * @return
	 */
	public String getType() {
		return type;
	}
	/**
	 * 获取option的名字
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * 获取option的排序值
	 * @return
	 */
	public int getOrder() {
		return order;
	}
}
