package com.douglei.bpm.process.api.user.option.impl.transfer;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.api.user.option.impl.delegate.DelegateOptionHandler;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.metadata.task.user.option.transfer.TransferOption;

/**
 * 
 * @author DougLei
 */
@Bean(clazz = OptionHandler.class)
public class TransferOptionHandler extends DelegateOptionHandler {
	
	@Override
	public String getType() {
		return OptionTypeConstants.TRANSFER;
	}

	@Override
	protected Option createOption(String name, int order, boolean reasonIsRequired, Candidate candidate) {
		return new TransferOption(OptionTypeConstants.TRANSFER, name, order, reasonIsRequired, candidate);
	}
}
