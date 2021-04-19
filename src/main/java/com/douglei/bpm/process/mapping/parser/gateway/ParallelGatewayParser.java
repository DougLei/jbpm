package com.douglei.bpm.process.mapping.parser.gateway;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.gateway.ParallelGatewayMetadata;
import com.douglei.bpm.process.mapping.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ParallelGatewayParser extends AbstractGatewayParser {

	@Override
	protected ParallelGatewayMetadata createGatewayMetadata(String id, String name, String defaultOutputFlowId, boolean[] variableExtend) {
		return new ParallelGatewayMetadata(id, name, defaultOutputFlowId, variableExtend);
	}

	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
