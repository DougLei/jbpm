package com.douglei.bpm.module.repository.definition.entity;

/**
 * 流程定义状态
 * @author DougLei
 */
public enum State {
	
	/**
	 * 初始化
	 */
	INITIAL(true, false, true, false){
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case DEPLOY:
				case DELETE:
					return true;
				default:
					return false;
			}
		}
	}, 
	
	/**
	 * 部署
	 */
	DEPLOY(false, true, false, false){
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case UNDEPLOY:
					return true;
				default:
					return false;
			}
		}
	}, 
	
	/**
	 * 未部署
	 */
	UNDEPLOY(true, false, true, false){
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case DEPLOY:
				case DELETE:
					return true;
				default:
					return false;
			}
		}
	}, 
	
	/**
	 * 无效
	 */
	INVALID(false, false, false, true),
	
	/**
	 * 删除
	 */
	DELETE(false, false, false, true){
		@Override
		public boolean supportConvert(State targetState) {
			switch(targetState) {
				case UNDEPLOY:
					return true;
				default:
					return false;
			}
		}
	}; 

	private boolean supportDeploy; // 当前状态是是否支持部署
	private boolean supportUnDeploy; // 当前状态是是否支持取消部署
	private boolean supportDelete; // 当前状态是是否支持删除
	private boolean supportPhysicalDelete; // 当前状态是是否支持物理删除
	private State(boolean supportDeploy, boolean supportUnDeploy, boolean supportDelete, boolean supportPhysicalDelete) {
		this.supportDeploy = supportDeploy;
		this.supportUnDeploy = supportUnDeploy;
		this.supportDelete = supportDelete;
		this.supportPhysicalDelete = supportPhysicalDelete;
	}
	
	public boolean supportDeploy() {
		return supportDeploy;
	}
	public boolean supportUnDeploy() {
		return supportUnDeploy;
	}
	public boolean supportDelete() {
		return supportDelete;
	}
	public boolean supportPhysicalDelete() {
		return supportPhysicalDelete;
	}
	
	/**
	 * 当前状态是否支持转换为目标状态
	 * @param targetState
	 * @return
	 */
	public boolean supportConvert(State targetState) {
		return false;
	}
}
