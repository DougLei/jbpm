package com.douglei.bpm.module.repository.definition;

/**
 * 流程定义状态
 * @author DougLei
 */
public enum State {
	
	/**
	 * 初始化: 1
	 */
	INITIAL(1) {
		@Override
		public boolean supportDeploy() {
			return true;
		}
		@Override
		public boolean supportDelete() {
			return true;
		}
	}, 
	
	/**
	 * 部署: 2
	 */
	DEPLOY(2) {
		@Override
		public boolean supportUnDeploy() {
			return true;
		}
	}, 
	
	/**
	 * 未部署: 3
	 */
	UNDEPLOY(3) {
		@Override
		public boolean supportDeploy() {
			return true;
		}
		@Override
		public boolean supportDelete() {
			return true;
		}
	}, 
	
	/**
	 * 无效: 4
	 */
	INVALID(4) {
		@Override
		public boolean supportPhysicalDelete() {
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
	 * 当前状态是是否支持部署
	 * @return
	 */
	public boolean supportDeploy() {
		return false;
	}
	
	/**
	 * 当前状态是是否支持取消部署
	 * @return
	 */
	public boolean supportUnDeploy() {
		return false;
	}
	
	/**
	 * 当前状态是是否支持删除
	 * @return
	 */
	public boolean supportDelete() {
		return false;
	}
	
	/**
	 * 当前状态是是否支持物理删除
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
	 * 根据标识值获取SourceType实例
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
