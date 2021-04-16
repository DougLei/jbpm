package com.douglei.bpm.module.history;

/**
 * 标识数据是因为什么, 进入到历史表
 * @author DougLei
 */
public enum SourceType {
	
	/**
	 * 标准进入, 即正常流转时进入历史表
	 */
	STANDARD(1),
	
	/**
	 * 因为流程实例被终止而进入历史表
	 */
	BY_PROCINST_TERMINATED(2);
	
	/**
	 * 获取标识值
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 根据标识值获取SourceType实例
	 * @param value
	 * @return
	 */
	public static SourceType valueOf(int value) {
		for (SourceType st : SourceType.values()) {
			if(st.value == value)
				return st;
		}
		throw new IllegalArgumentException("不存在value为["+value+"]的SourceType Enum");
	}
	
	// ---------------------------------------------------------------
	private int value;
	private SourceType(int value) {
		this.value = value;
	}
}
