package com.douglei.bpm.process.mapping.parser.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.gateway.ExclusiveGatewayMetadata;
import com.douglei.bpm.process.mapping.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ExclusiveGatewayParser extends AbstractGatewayParser {

	@Override
	protected ExclusiveGatewayMetadata createGatewayMetadata(String id, String name, String defaultOutputFlowId, boolean[] variableExtend) {
		return new ExclusiveGatewayMetadata(id, name, defaultOutputFlowId, variableExtend);
	}
	
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
