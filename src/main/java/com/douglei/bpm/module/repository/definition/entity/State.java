package com.douglei.bpm.module.repository.definition.entity;

/**
 * 
 * @author DougLei
 */
public enum State {
	
	// 初始化
	INITIAL() {
		@Override
		public boolean supportPhysicalDelete() {
			return true;
		}
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case INITIAL:
				case DEPLOY:
				case DELETE:
					return true;
				default:
					return false;
			}
		}
	}, 
	
	// 部署
	DEPLOY() {
		@Override
		public boolean supportStart() {
			return true;
		}
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case DEPLOY:
				case UNDEPLOY:
					return true;
				default:
					return false;
			}
		}
	}, 
	
	// 未部署
	UNDEPLOY() {
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case UNDEPLOY:
				case DEPLOY:
				case DELETE:
					return true;
				default:
					return false;
			}
		}
	}, 
	
	// 无效
	INVALID() {
		@Override
		public boolean supportPhysicalDelete() {
			return true;
		}
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case INVALID:
					return true;
				default:
					return false;
			}
		}
	}, 
	
	// 删除
	DELETE() {
		@Override
		public boolean supportPhysicalDelete() {
			return true;
		}
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case DELETE:
				case UNDEPLOY:
					return true;
				default:
					return false;
			}
		}
	}; 

	/**
	 * 当前状态是是否支持物理删除
	 * @return
	 */
	public boolean supportPhysicalDelete() {
		return false;
	}
	
	/**
	 * 当前状态是是否支持启动
	 * @return
	 */
	public boolean supportStart() {
		return false;
	}
	
	/**
	 * 当前状态是否支持转换为目标状态
	 * @param targetState
	 * @return
	 */
	public abstract boolean supportConvert(State targetState);
}
