package com.douglei.bpm.process.mapping.metadata.gateway;

import com.douglei.bpm.process.Type;

/**
 * 
 * @author DougLei
 */
public class ExclusiveGatewayMetadata extends AbstractGatewayMetadata {
	
	public ExclusiveGatewayMetadata(String id, String name, String defaultOutputFlowId, boolean[] variableExtend) {
		super(id, name, defaultOutputFlowId, variableExtend);
	}
	
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
