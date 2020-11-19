package com.douglei.bpm.process.parser.task.gateway;

import com.douglei.bpm.process.node.task.gateway.ParallelGateway;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class ParallelGatewayParser implements Parser<TaskMetadata, ParallelGateway> {

	@Override
	public String elementName() {
		return "parallelGateway";
	}

	@Override
	public ParallelGateway parse(TaskMetadata taskMetadata) throws ProcessParseException {
		return new ParallelGateway(taskMetadata.getId(), taskMetadata.getElement().attributeValue("name"));
	}
}
