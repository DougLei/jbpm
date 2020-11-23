package com.douglei.bpm.process.parser.task.gateway;

import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.process.node.task.gateway.InclusiveGateway;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class InclusiveGatewayParser implements Parser<TaskTemporaryData, InclusiveGateway> {

	@Override
	public String elementName() {
		return "inclusiveGateway";
	}

	@Override
	public InclusiveGateway parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new InclusiveGateway(temporaryData.getId(), temporaryData.getElement().attributeValue("name"));
	}
}
