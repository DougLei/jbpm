package com.douglei.bpm.process.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayMetadata extends AbstractGatewayMetadata {

	public ParallelGatewayMetadata(String id, String name, String defaultFlowId, int unextendScopeWeight) {
		super(id, name, defaultFlowId, unextendScopeWeight);
	}
	
	@Override
	public boolean supportFlowConditionExpr() {
		return false;
	}
	
	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
