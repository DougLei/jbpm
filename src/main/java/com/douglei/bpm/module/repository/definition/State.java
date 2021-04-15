package com.douglei.bpm.module.repository.definition;

/**
 * 流程定义状态
 * @author DougLei
 */
public enum State {
	
	/**
	 * 初始化
	 */
	INITIAL{
		
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
	 * 部署
	 */
	DEPLOY{
		
		@Override
		public boolean supportUnDeploy() {
			return true;
		}
	}, 
	
	/**
	 * 未部署
	 */
	UNDEPLOY {
		
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
	 * 无效
	 */
	INVALID {
		
		@Override
		public boolean supportPhysicalDelete() {
			return true;
		}
	},
	
	/**
	 * 删除
	 */
	DELETE {
		
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
}
