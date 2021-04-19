package com.douglei.bpm.process.mapping.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayMetadata extends AbstractGatewayMetadata {

	public ParallelGatewayMetadata(String id, String name, String defaultOutputFlowId, boolean[] variableExtend) {
		super(id, name, defaultOutputFlowId, variableExtend);
	}
	
	@Override
	public Type getType() {
		return Type.PARALLEL_GATEWAY;
	}
}
