package com.douglei.bpm.process.parser.impl.task.event;

import com.douglei.bpm.process.executer.task.event.EndEvent;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class EndEventParser implements Parser<TaskElement, EndEvent> {

	@Override
	public String elementName() {
		return "endEvent";
	}

	@Override
	public EndEvent parse(TaskElement taskElement) throws ProcessParseException {
		return new EndEvent(taskElement.getId(), taskElement.getName());
	}
}
