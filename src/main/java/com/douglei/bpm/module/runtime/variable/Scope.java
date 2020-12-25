package com.douglei.bpm.module.runtime.variable;

/**
 * 变量的范围
 * @author DougLei
 */
public enum Scope {
	GLOBAL(5),
	LOCAL(3),
	TRANSIENT(1);
	
	private int weight; // 权值
	private Scope(int weight) {
		this.weight = weight;
	}
	public int getWeight() {
		return weight;
	}
}
