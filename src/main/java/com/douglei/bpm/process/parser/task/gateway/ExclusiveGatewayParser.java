package com.douglei.bpm.process.parser.task.gateway;

import com.douglei.bpm.process.node.task.gateway.ExclusiveGateway;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class ExclusiveGatewayParser implements Parser<TaskMetadata, ExclusiveGateway> {

	@Override
	public String elementName() {
		return "exclusiveGateway";
	}

	@Override
	public ExclusiveGateway parse(TaskMetadata taskMetadata) throws ProcessParseException {
		return new ExclusiveGateway(taskMetadata.getId(), taskMetadata.getElement().attributeValue("name"));
	}
}
