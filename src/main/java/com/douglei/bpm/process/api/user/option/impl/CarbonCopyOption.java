package com.douglei.bpm.process.api.user.option.impl;

import com.douglei.bpm.process.api.user.option.Option;

/**
 * 
 * @author DougLei
 */
public class CarbonCopyOption implements Option{
	public static final String TYPE = "carbonCopy";
	
	@Override
	public String getType() {
		return TYPE;
	}

}
