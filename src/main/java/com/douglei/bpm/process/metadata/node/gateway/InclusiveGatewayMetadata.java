package com.douglei.bpm.process.metadata.node.gateway;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.TaskNodeMetadata;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayMetadata extends TaskNodeMetadata {

	public InclusiveGatewayMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public NodeType getType() {
		return NodeType.INCLUSIVE_GATEWAY;
	}
}
