package com.douglei.bpm.process.parser.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.gateway.ExclusiveGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.tmp.data.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ExclusiveGatewayParser implements Parser<TaskTemporaryData, ExclusiveGatewayMetadata> {

	@Override
	public ExclusiveGatewayMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new ExclusiveGatewayMetadata(temporaryData.getId(), temporaryData.getElement().attributeValue("name"), temporaryData.getElement().attributeValue("defaultFlow"));
	}

	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
