package com.douglei.bpm.process.parser.impl.task.gateway;

import com.douglei.bpm.process.executer.task.gateway.InclusiveGateway;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class InclusiveGatewayParser implements Parser<TaskElement, InclusiveGateway> {

	@Override
	public String elementName() {
		return "inclusiveGateway";
	}

	@Override
	public InclusiveGateway parse(TaskElement taskElement) throws ProcessParseException {
		return new InclusiveGateway(taskElement.getId(), taskElement.getElement().attributeValue("name"));
	}
}
