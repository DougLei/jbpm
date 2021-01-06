package com.douglei.bpm.process.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayMetadata extends AbstractGatewayMetadata {

	public ParallelGatewayMetadata(String id, String name, String defaultOutputFlowId, int unextendScopeWeight) {
		super(id, name, defaultOutputFlowId, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
