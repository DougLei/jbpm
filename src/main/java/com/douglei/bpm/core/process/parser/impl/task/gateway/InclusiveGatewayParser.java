package com.douglei.bpm.core.process.parser.impl.task.gateway;

import com.douglei.bpm.bean.annotation.ParserBean;
import com.douglei.bpm.core.process.executer.task.gateway.InclusiveGateway;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.parser.element.TaskElement;

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
		return new InclusiveGateway(taskElement.getId(), taskElement.getName());
	}
}
