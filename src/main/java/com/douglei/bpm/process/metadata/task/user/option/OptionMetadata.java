package com.douglei.bpm.process.metadata.task.user.option;

/**
 * 
 * @author DougLei
 */
public class OptionMetadata<T> {
	private String type;
	private String name;
	private int order;
	private T parameter; // 相关配置参数

	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public int getOrder() {
		return order;
	}
	public T getParameter() {
		return parameter;
	}
}
