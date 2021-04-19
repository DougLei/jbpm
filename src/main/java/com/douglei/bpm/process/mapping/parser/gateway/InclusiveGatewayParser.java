package com.douglei.bpm.process.mapping.parser.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.gateway.InclusiveGatewayMetadata;
import com.douglei.bpm.process.mapping.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class InclusiveGatewayParser extends AbstractGatewayParser {

	@Override
	protected InclusiveGatewayMetadata createGatewayMetadata(String id, String name, String defaultOutputFlowId, boolean[] variableExtend) {
		return new InclusiveGatewayMetadata(id, name, defaultOutputFlowId, variableExtend);
	}
	
	@Override
	public Type getType() {
		return Type.INCLUSIVE_GATEWAY;
	}
}
