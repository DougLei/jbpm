package com.douglei.bpm.process.parser.impl.task.gateway;

import com.douglei.bpm.process.executer.task.gateway.ExclusiveGateway;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class ExclusiveGatewayParser implements Parser<TaskElement, ExclusiveGateway> {

	@Override
	public String elementName() {
		return "exclusiveGateway";
	}

	@Override
	public ExclusiveGateway parse(TaskElement taskElement) throws ProcessParseException {
		return new ExclusiveGateway(taskElement.getId(), taskElement.getName());
	}
}
