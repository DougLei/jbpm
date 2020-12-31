package com.douglei.bpm.process.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayMetadata extends AbstractGatewayMetadata {

	public ParallelGatewayMetadata(String id, String name, int unextendScopeWeight) {
		super(id, name, unextendScopeWeight);
	}
	
	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
