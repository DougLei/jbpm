package com.douglei.bpm.module.execution.instance;

/**
 * 
 * @author DougLei
 */
public enum State {
	
	/**
	 * 活动中: 1
	 */
	ACTIVE(1),
	
	/**
	 * 挂起: 2
	 */
	SUSPENDED(2),
	
	/**
	 * 终止: 3
	 */
	TERMINATED(3) {
		@Override
		public boolean supportDelete() {
			return true;
		}
	},
	
	/**
	 * 结束: 4
	 */
	FINISHED(4) {
		@Override
		public boolean supportDelete() {
			return true;
		}
	},
	
	/**
	 * 删除: 5
	 */
	DELETE(5) { 
		@Override
		public boolean supportPhysicalDelete() {
			return true;
		}
	};
	
	/**
	 * 当前状态是否支持进行删除操作
	 * @return
	 */
	public boolean supportDelete() {
		return false;
	}

	/**
	 * 当前状态是否支持进行物理删除操作; 如果支持该操作, 也会支持撤销删除操作
	 * @return
	 */
	public boolean supportPhysicalDelete() {
		return false;
	}
	
	// ---------------------------------------------------------------
	private int value;
	private State(int value) {
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
	 * 根据标识值获取State实例
	 * @param value
	 * @return
	 */
	public static State valueOf(int value) {
		for (State state : State.values()) {
			if(state.value == value)
				return state;
		}
		throw new IllegalArgumentException("不存在value为["+value+"]的State Enum");
	}
}
