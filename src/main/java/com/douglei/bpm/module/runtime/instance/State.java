package com.douglei.bpm.module.runtime.instance;

/**
 * 
 * @author DougLei
 */
public enum State {
	
	/**
	 * 活动中: 1
	 */
	ACTIVE(1) {
		@Override
		public boolean supportTerminate() {
			return true;
		}
	},
	
	/**
	 * 挂起: 2
	 */
	SUSPENDED(2) {
		@Override
		public boolean supportTerminate() {
			return true;
		}
	},
	
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
	
	// value>6时, 均表示删除状态
	/**
	 * 终止状态时删除: 7
	 */
	TERMINATED_DELETE(TERMINATED.value + 4) { 
		@Override
		public boolean supportPhysicalDelete() {
			return true;
		}
	},
	/**
	 * 结束状态时删除: 8
	 */
	FINISHED_DELETE(FINISHED.value + 4) { 
		@Override
		public boolean supportPhysicalDelete() {
			return true;
		}
	};
	
	/**
	 * 获取标识值
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 当前状态是是否支持进行终止操作
	 * @return
	 */
	public boolean supportTerminate() {
		return false;
	}
	
	/**
	 * 当前状态是是否支持进行删除操作
	 * @return
	 */
	public boolean supportDelete() {
		return false;
	}
	
	/**
	 * 当前状态是是否支持进行物理删除操作
	 * @return
	 */
	public boolean supportPhysicalDelete() {
		return false;
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
	
	// ---------------------------------------------------------------
	private int value;
	private State(int value) {
		this.value = value;
	}
}
