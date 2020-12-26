package com.douglei.bpm.process.parser.gateway;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.gateway.AbstractGatewayMetadata;
import com.douglei.bpm.process.metadata.gateway.InclusiveGatewayMetadata;
import com.douglei.bpm.process.metadata.gateway.ParallelGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class InclusiveGatewayParser extends AbstractGatewayParser {

	@Override
	protected AbstractGatewayMetadata createGatewayMetadata(String id, String name, String defaultFlowId, int unextendScopeWeight, Element element) {
		if(Boolean.parseBoolean(element.attributeValue("enabledParallelMode")))
			return new ParallelGatewayMetadata(id, name, defaultFlowId, unextendScopeWeight);
		return new InclusiveGatewayMetadata(id, name, defaultFlowId, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.INCLUSIVE_GATEWAY;
	}
}
