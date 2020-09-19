package com.douglei.bpm.core.process.parser.impl.task.event;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.core.process.executer.task.event.StartEvent;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.parser.element.TaskElement;

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
		// TODO Auto-generated method stub
		
		taskElement.getId();
		taskElement.getName();
		taskElement.getElement();
		
		
		return null;
	}
}
