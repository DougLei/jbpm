package com.douglei.bpm.process.parser.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.gateway.ParallelGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.tmp.data.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ParallelGatewayParser implements Parser<TaskTemporaryData, ParallelGatewayMetadata> {

	@Override
	public ParallelGatewayMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new ParallelGatewayMetadata(temporaryData.getId(), temporaryData.getElement().attributeValue("name"), temporaryData.getElement().attributeValue("defaultFlow"));
	}

	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
