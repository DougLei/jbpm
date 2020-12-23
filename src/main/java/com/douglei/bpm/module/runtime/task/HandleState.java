package com.douglei.bpm.module.runtime.task;

/**
 * 指派人的办理状态
 * @author DougLei
 */
public enum HandleState {
	
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
	 * 无效
	 */
	INVALID {
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
	FINISHED{
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
