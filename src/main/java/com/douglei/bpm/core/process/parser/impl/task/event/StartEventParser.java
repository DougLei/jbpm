package com.douglei.bpm.core.process.parser.impl.task.event;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.executer.task.event.StartEvent;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
@Bean(transaction = false)
public class StartEventParser implements Parser<Element, StartEvent> {

	@Override
	public String elementName() {
		return "startEvent";
	}

	@Override
	public StartEvent parse(Element parameter) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
