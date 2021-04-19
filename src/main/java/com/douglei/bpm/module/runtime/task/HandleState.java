package com.douglei.bpm.module.runtime.task;

/**
 * 指派人的办理状态
 * @author DougLei
 */
public enum HandleState {
	
	/**
	 * 无效, 再也无法使用的状态
	 */
	INVALID(1),
	
	/**
	 * 无效的未认领
	 */
	INVALID_UNCLAIM(2),
	
	/**
	 * 可竞争的未认领
	 */
	COMPETITIVE_UNCLAIM(3),
	
	/**
	 * 未认领
	 */
	UNCLAIM(4),
	
	/**
	 * 已认领
	 */
	CLAIMED(5),
	
	/**
	 * 办理完成
	 */
	FINISHED(6);
	
	// ---------------------------------------------------------------
	private int value;
	private HandleState(int value) {
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
	 * 根据标识值获取HandleState实例
	 * @param value
	 * @return
	 */
	public static HandleState valueOf(int value) {
		for (HandleState hs : HandleState.values()) {
			if(hs.value == value)
				return hs;
		}
		throw new IllegalArgumentException("不存在value为["+value+"]的HandleState Enum");
	}
}
