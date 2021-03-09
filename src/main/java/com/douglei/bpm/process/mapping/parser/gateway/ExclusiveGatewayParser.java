package com.douglei.bpm.process.mapping.parser.gateway;

import org.dom4j.Element;

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
	protected ExclusiveGatewayMetadata createGatewayMetadata(String id, String name, String defaultOutputFlowId, int unextendScopeWeight, Element element) {
		return new ExclusiveGatewayMetadata(id, name, defaultOutputFlowId, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
