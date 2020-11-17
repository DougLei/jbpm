package com.douglei.bpm.process.parser.task.gateway;

import com.douglei.bpm.process.executor.task.gateway.InclusiveGateway;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class InclusiveGatewayParser implements Parser<TaskMetadata, InclusiveGateway> {

	@Override
	public String elementName() {
		return "inclusiveGateway";
	}

	@Override
	public InclusiveGateway parse(TaskMetadata taskMetadata) throws ProcessParseException {
		return new InclusiveGateway(taskMetadata.getId(), taskMetadata.getElement().attributeValue("name"));
	}
}
