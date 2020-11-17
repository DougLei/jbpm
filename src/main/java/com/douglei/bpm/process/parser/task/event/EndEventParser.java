package com.douglei.bpm.process.parser.task.event;

import com.douglei.bpm.process.executor.task.event.EndEvent;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class EndEventParser implements Parser<TaskMetadata, EndEvent> {

	@Override
	public String elementName() {
		return "endEvent";
	}

	@Override
	public EndEvent parse(TaskMetadata taskMetadata) throws ProcessParseException {
		return new EndEvent(taskMetadata.getId(), taskMetadata.getElement().attributeValue("name"));
	}
}
