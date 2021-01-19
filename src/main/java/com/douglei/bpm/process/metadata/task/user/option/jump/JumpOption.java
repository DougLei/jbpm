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
	private boolean suggest; // 是否需要强制输入意见
	private boolean attitude; // 是否需要强制表态
	
	public JumpOption(String type, String name, int order, boolean reason, String target, boolean suggest, boolean attitude, Candidate candidate) {
		super(type, name, order, reason, candidate);
		this.target = target;
		this.suggest = suggest;
		this.attitude = attitude;
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
	public boolean isSuggest() {
		return suggest;
	}
	/**
	 * 是否需要强制表态
	 * @return
	 */
	public boolean isAttitude() {
		return attitude;
	}
	
	@Override
	public ActiveTime getActiveTime() {
		return ActiveTime.TASK_HANDLING;
	}
}
