package com.douglei.bpm.process.mapping.metadata.task.user.option.dispatch;

import com.douglei.bpm.process.mapping.metadata.task.user.option.ActiveTime;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractDispatchOption extends Option {
	private boolean suggestIsRequired; // 是否需要强制输入意见
	private boolean attitudeIsRequired; // 是否需要强制表态
	
	public AbstractDispatchOption(String type, String name, int order, boolean suggestIsRequired, boolean attitudeIsRequired) {
		super(type, name, order);
		this.suggestIsRequired = suggestIsRequired;
		this.attitudeIsRequired = attitudeIsRequired;
	}
	
	/**
	 * 是否需要强制输入意见
	 * @return
	 */
	public boolean suggestIsRequired() {
		return suggestIsRequired;
	}
	/**
	 * 是否需要强制表态
	 * @return
	 */
	public boolean attitudeIsRequired() {
		return attitudeIsRequired;
	}

	@Override
	public ActiveTime getActiveTime() {
		return ActiveTime.TASK_DISPATCHING;
	}
}
