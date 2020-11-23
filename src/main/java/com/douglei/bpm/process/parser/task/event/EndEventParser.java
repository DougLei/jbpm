package com.douglei.bpm.process.parser.task.event;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.node.task.event.EndEvent;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class EndEventParser implements Parser<TaskTemporaryData, EndEvent> {

	@Override
	public String elementName() {
		return "endEvent";
	}

	@Override
	public EndEvent parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new EndEvent(temporaryData.getId(), temporaryData.getElement().attributeValue("name"));
	}
}
