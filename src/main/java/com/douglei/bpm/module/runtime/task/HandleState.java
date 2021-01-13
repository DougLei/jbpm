package com.douglei.bpm.module.runtime.task;

/**
 * 指派人的办理状态
 * @author DougLei
 */
public enum HandleState {
	
	/**
	 * 无效, 再也无法使用的状态
	 */
	INVALID,
	
	/**
	 * 可竞争的未认领
	 */
	COMPETITIVE_UNCLAIM {
		@Override
		public boolean unClaim() {
			return true;
		}
	},
	
	/**
	 * 未认领
	 */
	UNCLAIM {
		@Override
		public boolean unClaim() {
			return true;
		}
	},
	
	/**
	 * 已认领
	 */
	CLAIMED {
		@Override
		public boolean isClaimed() {
			return true;
		}
	},
	
	/**
	 * 办理完成
	 */
	FINISHED {
		@Override
		public boolean isClaimed() {
			return true;
		}
		@Override
		public boolean isFinished() {
			return true;
		}
	};
	
	/**
	 * 是否未认领
	 * @return
	 */
	public boolean unClaim() {
		return false;
	}
	/**
	 * 是否已认领
	 * @return
	 */
	public boolean isClaimed() {
		return false;
	}
	/**
	 * 是否已经完成
	 * @return
	 */
	public boolean isFinished() {
		return false;
	}
}
