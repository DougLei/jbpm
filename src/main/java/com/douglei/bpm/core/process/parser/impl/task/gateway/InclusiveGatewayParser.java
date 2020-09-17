package com.douglei.bpm.core.process.parser.impl.task.gateway;

import org.dom4j.Element;

import com.douglei.bpm.core.process.executer.flow.Flow;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayParser implements Parser<Element, Flow> {

	@Override
	public String elementName() {
		return "inclusiveGateway";
	}

	@Override
	public Flow parse(Element parameter) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
