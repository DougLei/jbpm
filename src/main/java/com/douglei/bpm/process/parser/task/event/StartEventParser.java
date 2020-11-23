package com.douglei.bpm.process.parser.task.event;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.node.task.event.StartEvent;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class StartEventParser implements Parser<TaskTemporaryData, StartEvent> {
	
	@Override
	public String elementName() {
		return "startEvent";
	}

	@Override
	public StartEvent parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		return new StartEvent(temporaryData.getId(), element.attributeValue("name"), element.attributeValue("conditionExpr"));
	}
}
