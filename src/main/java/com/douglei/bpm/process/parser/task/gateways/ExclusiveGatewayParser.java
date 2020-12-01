package com.douglei.bpm.process.parser.task.gateways;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.gateways.ExclusiveGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ExclusiveGatewayParser implements Parser<TaskTemporaryData, ExclusiveGatewayMetadata> {

	@Override
	public ExclusiveGatewayMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new ExclusiveGatewayMetadata(temporaryData.getId(), temporaryData.getElement().attributeValue("name"));
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.EXCLUSIVE_GATEWAY;
	}
}
