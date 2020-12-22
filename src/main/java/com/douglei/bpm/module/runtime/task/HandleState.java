package com.douglei.bpm.module.runtime.task;

/**
 * 指派人的办理状态
 * @author DougLei
 */
public enum HandleState {
	
	/**
	 * 未认领
	 */
	UNCLAIM,
	
	/**
	 * 无效
	 */
	INVALID,
	
	/**
	 * 已认领
	 */
	CLAIM {
		@Override
		public boolean isClaim() {
			return true;
		}
	},
	
	/**
	 * 办理完成
	 */
	FINISHED{
		@Override
		public boolean isClaim() {
			return true;
		}
	};
	
	/**
	 * 是否已认领
	 * @return
	 */
	public boolean isClaim() {
		return false;
	}
}
