package com.douglei.bpm.module.runtime.task;

/**
 * 指派的模式
 * @author DougLei
 */
public enum AssignMode {
	
	/**
	 * 静态的办理人
	 */
	STATIC("静态指派"),
	
	/**
	 * 指派的办理人
	 */
	ASSIGNED("动态指派"),
	
	/**
	 * 委托的办理人
	 */
	DELEGATED("委托"),
	
	/**
	 * 移交的办理人
	 */
	TRANSFERRED("转办");
	
	private String name;
	private AssignMode(String name) {
		this.name = name;
	}
	
	/**
	 * 获取模式的名称
	 * @return
	 */
	public String getName() {
		return name;
	}
}
