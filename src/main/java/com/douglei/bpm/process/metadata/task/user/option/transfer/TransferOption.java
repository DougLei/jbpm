package com.douglei.bpm.process.metadata.task.user.option.transfer;

import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.delegate.DelegateOption;

/**
 * 
 * @author DougLei
 */
public class TransferOption extends DelegateOption {

	public TransferOption(String type, String name, int order, Candidate candidate) {
		super(type, name, order, candidate);
	}
}
