package com.douglei.bpm.process.api.user.option.impl.transfer;

import com.douglei.bpm.process.api.user.option.impl.delegate.DelegateOptionHandler;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.transfer.TransferOption;

/**
 * 
 * @author DougLei
 */
public class TransferOptionHandler extends DelegateOptionHandler {
	public static final String TYPE = "transfer";
	
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	protected Option createOption(String name, int order, Candidate candidate) {
		return new TransferOption(TYPE, name, order, candidate);
	}
}
