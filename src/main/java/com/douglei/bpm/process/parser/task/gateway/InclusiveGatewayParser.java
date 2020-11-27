package com.douglei.bpm.process.parser.task.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.gateway.InclusiveGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class InclusiveGatewayParser implements Parser<TaskTemporaryData, InclusiveGatewayMetadata> {

	@Override
	public InclusiveGatewayMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new InclusiveGatewayMetadata(temporaryData.getId(), temporaryData.getElement().attributeValue("name"), getNodeType());
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.INCLUSIVE_GATEWAY;
	}
}
