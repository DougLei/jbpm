package com.douglei.bpm.process.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayMetadata extends AbstractGatewayMetadata {
	
	public InclusiveGatewayMetadata(String id, String name, int unextendScopeWeight) {
		super(id, name, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.INCLUSIVE_GATEWAY;
	}
}
