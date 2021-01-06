package com.douglei.bpm.process.parser.gateway;

import org.dom4j.Element;

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
	protected InclusiveGatewayMetadata createGatewayMetadata(String id, String name, String defaultOutputFlowId, int unextendScopeWeight, Element element) {
		return new InclusiveGatewayMetadata(id, name, defaultOutputFlowId, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.INCLUSIVE_GATEWAY;
	}
}
