package com.douglei.bpm.process.metadata.task.user.option.carboncopy;

import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.ActiveTime;
import com.douglei.bpm.process.metadata.task.user.option.Option;

/**
 * 
 * @author DougLei
 */
public class CarbonCopyOption extends Option {
	private Candidate candidate;
	
	public CarbonCopyOption(String type, String name, int order, Candidate candidate) {
		super(type, name, order);
		this.candidate = candidate;
	}

	/**
	 * 获取候选人配置
	 * @return
	 */
	public Candidate getCandidate() {
		return candidate;
	}

	@Override
	public ActiveTime getActiveTime() {
		return ActiveTime.TASK_HANDLING;
	}
}
