package com.douglei.bpm.process.parser.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.gateway.InclusiveGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class InclusiveGatewayParser extends AbstractGatewayParser {

	@Override
	protected InclusiveGatewayMetadata createGatewayMetadata(String id, String name, String defaultFlowId, int excludeScopeWeight) {
		return new InclusiveGatewayMetadata(id, name, defaultFlowId, excludeScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.INCLUSIVE_GATEWAY;
	}
}
