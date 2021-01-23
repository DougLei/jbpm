package com.douglei.bpm.process.metadata.task.user.option.jump;

import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.ActiveTime;
import com.douglei.bpm.process.metadata.task.user.option.delegate.DelegateOption;

/**
 * 
 * @author DougLei
 */
public class JumpOption extends DelegateOption {
	private String target;
	private boolean suggestIsRequired; // 是否需要强制输入意见
	private boolean attitudeIsRequired; // 是否需要强制表态
	
	public JumpOption(String type, String name, int order, boolean reasonIsRequired, String target, boolean suggestIsRequired, boolean attitudeIsRequired, Candidate candidate) {
		super(type, name, order, reasonIsRequired, candidate);
		this.target = target;
		this.suggestIsRequired = suggestIsRequired;
		this.attitudeIsRequired = attitudeIsRequired;
	}
	
	/**
	 * 获取要跳转的目标id
	 * @return
	 */
	public String getTarget() {
		return target;
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
		return ActiveTime.TASK_HANDLING;
	}
}
