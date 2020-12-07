package com.douglei.bpm.process.metadata.node.gateway;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayMetadata extends TaskMetadata {

	public InclusiveGatewayMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public Type getType() {
		return Type.INCLUSIVE_GATEWAY;
	}
}
