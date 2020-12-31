package com.douglei.bpm.process.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class ExclusiveGatewayMetadata extends AbstractGatewayMetadata {
	private String defaultOutputFlowId; 
	
	public ExclusiveGatewayMetadata(String id, String name, String defaultOutputFlowId, int unextendScopeWeight) {
		super(id, name, unextendScopeWeight);
		this.defaultOutputFlowId = defaultOutputFlowId;
	}
	
	@Override
	public final String getDefaultOutputFlowId() {
		return defaultOutputFlowId;
	}
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
