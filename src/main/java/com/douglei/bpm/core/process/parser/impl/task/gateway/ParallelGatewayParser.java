package com.douglei.bpm.core.process.parser.impl.task.gateway;

import com.douglei.bpm.bean.annotation.ParserBean;
import com.douglei.bpm.core.process.executer.task.gateway.ParallelGateway;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class ParallelGatewayParser implements Parser<TaskElement, ParallelGateway> {

	@Override
	public String elementName() {
		return "parallelGateway";
	}

	@Override
	public ParallelGateway parse(TaskElement taskElement) throws ProcessParseException {
		return new ParallelGateway(taskElement.getId(), taskElement.getName());
	}
}
