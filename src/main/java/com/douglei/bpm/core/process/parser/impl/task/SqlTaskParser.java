package com.douglei.bpm.core.process.parser.impl.task;

import org.dom4j.Element;

import com.douglei.bpm.core.process.executer.flow.Flow;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
public class SqlTaskParser implements Parser<Element, Flow> {

	@Override
	public String elementName() {
		return "sqlTask";
	}

	@Override
	public Flow parse(Element parameter) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
