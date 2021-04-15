package com.douglei.bpm.module.runtime.instance;

/**
 * 
 * @author DougLei
 */
public enum State {
	
	/**
	 * 活动中
	 */
	ACTIVE{
		@Override
		public boolean supportTerminate() {
			return true;
		}
	},
	
	/**
	 * 挂起
	 */
	SUSPENDED{
		@Override
		public boolean supportTerminate() {
			return true;
		}
	},
	
	/**
	 * 终止
	 */
	TERMINATED {
		@Override
		public boolean supportDelete() {
			return true;
		}
	},
	
	/**
	 * 结束
	 */
	FINISHED {
		@Override
		public boolean supportDelete() {
			return true;
		}
	},
	
	/**
	 * 删除
	 */
	DELETE{
		@Override
		public boolean supportPhysicalDelete() {
			return true;
		}
	};
	
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
}
