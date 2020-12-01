package com.douglei.bpm.process.parser.task.gateways;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.gateways.ParallelGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ParallelGatewayParser implements Parser<TaskTemporaryData, ParallelGatewayMetadata> {

	@Override
	public ParallelGatewayMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new ParallelGatewayMetadata(temporaryData.getId(), temporaryData.getElement().attributeValue("name"));
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.PARALLEL_GATEWAY;
	}
}
