package com.douglei.bpm.module.runtime.task;

/**
 * 指派的模式
 * @author DougLei
 */
public enum AssignMode {
	
	/**
	 * 固定的办理人
	 */
	FIXED(1),
	
	/**
	 * 指派的办理人
	 */
	ASSIGNED(2),
	
	/**
	 * 委托的办理人
	 */
	DELEGATED(3),
	
	/**
	 * 移交的办理人
	 */
	TRANSFERRED(4);
	
	// ---------------------------------------------------------------
	private int value;
	private AssignMode(int value) {
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
	 * 根据标识值获取AssignMode实例
	 * @param value
	 * @return
	 */
	public static AssignMode valueOf(int value) {
		for (AssignMode am : AssignMode.values()) {
			if(am.value == value)
				return am;
		}
		throw new IllegalArgumentException("不存在value为["+value+"]的AssignMode Enum");
	}
}
