package com.douglei.bpm.process.api.user.task.handle.policy;

import com.douglei.bpm.bean.annotation.MultiInstance;

/**
 * (判断)任务是否可以结束的策略
 * @author DougLei
 */
@MultiInstance
public interface CanFinishPolicy {

	/**
	 * 获取策略名称, 必须唯一; 默认值为类名全路径
	 * @return
	 */
	default String getName() {
		return getClass().getName();
	}
	
	
	boolean canFinish();
}
