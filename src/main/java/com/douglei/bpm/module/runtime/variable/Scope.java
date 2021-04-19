package com.douglei.bpm.module.runtime.variable;

/**
 * 变量的范围
 * @author DougLei
 */
public enum Scope {
	GLOBAL(1),
	LOCAL(2),
	TRANSIENT(3);
	
	// ---------------------------------------------------------------
	private int value; // 
	private Scope(int value) {
		this.value = value;
	}
	
	/**
	 * 获取标识值
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 根据标识值获取Scope实例
	 * @param value
	 * @return
	 */
	public static Scope valueOf(int value) {
		for (Scope scope : Scope.values()) {
			if(scope.value == value)
				return scope;
		}
		throw new IllegalArgumentException("不存在value为["+value+"]的Scope Enum");
	}
}
