package com.douglei.bpm.component.variable;

/**
 * 变量的范围
 * @author DougLei
 */
public enum Scope {
	/**
	 * 全局
	 */
	GLOBAL(1),
	/**
	 * 本地
	 */
	LOCAL(2),
	/**
	 * 瞬时
	 */
	TRANSIENT(3);
	
	private int value;
	private Scope(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
