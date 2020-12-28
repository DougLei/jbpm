package com.douglei.bpm.process.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayMetadata extends AbstractGatewayMetadata {
	
	public InclusiveGatewayMetadata(String id, String name, String defaultFlowId, int unextendScopeWeight) {
		super(id, name, defaultFlowId, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.INCLUSIVE_GATEWAY;
	}
}