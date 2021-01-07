package com.douglei.bpm.process.api.user.task.handle.policy;

import com.douglei.bpm.bean.annotation.MultiInstance;

/**
 * 串行办理任务时的办理顺序策略
 * @author DougLei
 */
@MultiInstance
public interface SerialHandleSequencePolicy {

	/**
	 * 获取策略名称, 必须唯一; 默认值为类名全路径
	 * @return
	 */
	default String getName() {
		return getClass().getName();
	}
	
	
}
