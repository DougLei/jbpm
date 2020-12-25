package com.douglei.bpm.process.metadata.node.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class ExclusiveGatewayMetadata extends AbstractGatewayMetadata {

	public ExclusiveGatewayMetadata(String id, String name, String defaultFlowId, int unextendScopeWeight) {
		super(id, name, defaultFlowId, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
