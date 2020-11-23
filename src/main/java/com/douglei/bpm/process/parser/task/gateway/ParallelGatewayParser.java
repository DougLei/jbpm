package com.douglei.bpm.process.parser.task.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.node.task.gateway.ParallelGateway;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ParallelGatewayParser implements Parser<TaskTemporaryData, ParallelGateway> {

	@Override
	public String elementName() {
		return "parallelGateway";
	}

	@Override
	public ParallelGateway parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new ParallelGateway(temporaryData.getId(), temporaryData.getElement().attributeValue("name"));
	}
}
