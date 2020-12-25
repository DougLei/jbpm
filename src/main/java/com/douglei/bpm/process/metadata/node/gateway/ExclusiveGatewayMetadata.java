package com.douglei.bpm.process.metadata.node.gateway;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.MultiFlowTaskMetadata;

/**
 * 
 * @author DougLei
 */
public class ExclusiveGatewayMetadata extends MultiFlowTaskMetadata {

	public ExclusiveGatewayMetadata(String id, String name, String defaultFlowId) {
		super(id, name, defaultFlowId);
	}
	
	@Override
	public Type getType() {
		return Type.EXCLUSIVE_GATEWAY;
	}
}
