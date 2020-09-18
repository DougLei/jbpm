package com.douglei.bpm.core.process.parser.impl.flow;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.executer.flow.Flow;
import com.douglei.bpm.core.process.parser.FlowElement;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
@Bean(transaction = false)
public class FlowParser implements Parser<FlowElement, Flow> {

	@Override
	public String elementName() {
		return "flow";
	}

	@Override
	public Flow parse(FlowElement parameter) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
