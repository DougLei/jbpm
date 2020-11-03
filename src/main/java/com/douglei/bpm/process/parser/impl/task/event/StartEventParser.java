package com.douglei.bpm.process.parser.impl.task.event;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.executer.task.event.StartEvent;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@Bean(transaction = false)
public class StartEventParser implements Parser<TaskElement, StartEvent> {

	@Override
	public String elementName() {
		return "startEvent";
	}

	@Override
	public StartEvent parse(TaskElement taskElement) throws ProcessParseException {
		return new StartEvent(taskElement.getId(), taskElement.getElement().attributeValue("name"));
	}
}
