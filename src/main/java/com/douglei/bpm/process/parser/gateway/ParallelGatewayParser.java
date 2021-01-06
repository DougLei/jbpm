package com.douglei.bpm.process.parser.gateway;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.gateway.ParallelGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ParallelGatewayParser extends AbstractGatewayParser {

	@Override
	protected ParallelGatewayMetadata createGatewayMetadata(String id, String name, String defaultOutputFlowId, int unextendScopeWeight, Element element) {
		return new ParallelGatewayMetadata(id, name, defaultOutputFlowId, unextendScopeWeight);
	}

	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
