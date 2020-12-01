package com.douglei.bpm.process.metadata.node.task.gateways;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class InclusiveGatewayMetadata extends TaskMetadata {

	public InclusiveGatewayMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public NodeType getType() {
		return NodeType.INCLUSIVE_GATEWAY;
	}
}
