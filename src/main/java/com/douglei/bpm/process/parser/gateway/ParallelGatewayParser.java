package com.douglei.bpm.process.parser.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.gateway.ParallelGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ParallelGatewayParser extends AbstractGatewayParser {

	@Override
	protected ParallelGatewayMetadata createGatewayMetadata(String id, String name, String defaultFlowId, int excludeScopeWeight) {
		return new ParallelGatewayMetadata(id, name, defaultFlowId, excludeScopeWeight);
	}

	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
