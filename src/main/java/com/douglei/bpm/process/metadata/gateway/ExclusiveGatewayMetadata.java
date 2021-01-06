package com.douglei.bpm.process.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class ExclusiveGatewayMetadata extends AbstractGatewayMetadata {
	
	public ExclusiveGatewayMetadata(String id, String name, int unextendScopeWeight) {
		super(id, name, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
