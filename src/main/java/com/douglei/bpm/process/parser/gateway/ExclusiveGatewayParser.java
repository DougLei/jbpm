package com.douglei.bpm.process.parser.gateway;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.gateway.ExclusiveGatewayMetadata;
import com.douglei.bpm.process.parser.Parser;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class ExclusiveGatewayParser extends AbstractGatewayParser {

	@Override
	protected ExclusiveGatewayMetadata createGatewayMetadata(String id, String name, int unextendScopeWeight, Element element) {
		return new ExclusiveGatewayMetadata(id, name, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
