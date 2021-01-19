package com.douglei.bpm.process.metadata.task.user.option.delegate;

import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;

/**
 * 
 * @author DougLei
 */
public class DelegateOption extends CarbonCopyOption {
	private boolean reason; // 是否必须输入原因
	
	public DelegateOption(String type, String name, int order, boolean reason, Candidate candidate) {
		super(type, name, order, candidate);
		this.reason = reason;
	}

	/**
	 * 是否必须输入原因
	 * @return
	 */
	public final boolean isReason() {
		return reason;
	}
}
