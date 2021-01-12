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
	private String prefix; // 前缀
	private Scope(int weight) {
		this.weight = weight;
		this.prefix = name().toLowerCase()+".";
	}
	public int getWeight() {
		return weight;
	}
	public String getPrefix() {
		return prefix;
	}
}
