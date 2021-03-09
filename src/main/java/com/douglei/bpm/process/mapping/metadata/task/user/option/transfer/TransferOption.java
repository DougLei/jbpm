package com.douglei.bpm.process.mapping.metadata.task.user.option.transfer;

import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.option.delegate.DelegateOption;

/**
 * 
 * @author DougLei
 */
public class TransferOption extends DelegateOption {

	public TransferOption(String type, String name, int order, boolean reasonIsRequired, Candidate candidate) {
		super(type, name, order, reasonIsRequired, candidate);
	}
}
