package com.douglei.bpm.module.components.variable;

/**
 * 变量的范围
 * @author DougLei
 */
public enum Scope {
	GLOBAL(1),
	LOCAL(2);
	
	private int value;
	private Scope(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
