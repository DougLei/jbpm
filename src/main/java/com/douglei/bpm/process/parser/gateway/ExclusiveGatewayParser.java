package com.douglei.bpm.process.parser.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.gateway.ExclusiveGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ExclusiveGatewayParser extends AbstractGatewayParser {

	@Override
	protected ExclusiveGatewayMetadata createGatewayMetadata(String id, String name, String defaultFlowId, int excludeScopeWeight) {
		return new ExclusiveGatewayMetadata(id, name, defaultFlowId, excludeScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
