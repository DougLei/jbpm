package com.douglei.bpm.core.process.parser.impl;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.executer.Process;

/**
 * process解析器
 * @author DougLei
 */
@Bean(transaction = false)
public class ProcessParser implements Parser<String, Process>{

	@Override
	public Process parse(String content) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
