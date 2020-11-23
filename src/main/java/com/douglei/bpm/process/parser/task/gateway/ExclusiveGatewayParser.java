package com.douglei.bpm.process.parser.task.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.node.task.gateway.ExclusiveGateway;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ExclusiveGatewayParser implements Parser<TaskTemporaryData, ExclusiveGateway> {

	@Override
	public String elementName() {
		return "exclusiveGateway";
	}

	@Override
	public ExclusiveGateway parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new ExclusiveGateway(temporaryData.getId(), temporaryData.getElement().attributeValue("name"));
	}
}
