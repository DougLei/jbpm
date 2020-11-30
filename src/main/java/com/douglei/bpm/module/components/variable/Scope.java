package com.douglei.bpm.module.components.variable;

/**
 * 变量的范围
 * @author DougLei
 */
public enum Scope {
	/**
	 * 全局
	 */
	GLOBAL(10),
	/**
	 * 本地
	 */
	LOCAL(20),
	/**
	 * 瞬时
	 */
	TRANSIENT(21);
	
	private int value;
	private Scope(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
