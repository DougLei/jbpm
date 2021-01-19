package com.douglei.bpm.process.api.user.option.impl.transfer;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.api.user.option.impl.delegate.DelegateOptionHandler;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.transfer.TransferOption;

/**
 * 
 * @author DougLei
 */
@Bean(clazz = OptionHandler.class)
public class TransferOptionHandler extends DelegateOptionHandler {
	public static final String TYPE = "transfer";
	
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	protected Option createOption(String name, int order, boolean reason, Candidate candidate) {
		return new TransferOption(TYPE, name, order, reason, candidate);
	}
}
