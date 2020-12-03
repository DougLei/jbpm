package com.douglei.bpm.module.runtime.task.entity.variable;

/**
 * 变量的范围
 * @author DougLei
 */
public enum Scope {
	GLOBAL,
	LOCAL,
	TRANSIENT;
	
	private String value;
	private Scope() {
		this.value = name().toLowerCase();
	}

	public String getValue() {
		return value;
	}
}
