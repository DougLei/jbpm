package com.douglei.bpm.process.metadata.task.user.option.delegate;

import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;

/**
 * 
 * @author DougLei
 */
public class DelegateOption extends CarbonCopyOption {

	public DelegateOption(String type, String name, int order, Candidate candidate) {
		super(type, name, order, candidate);
	}
}
