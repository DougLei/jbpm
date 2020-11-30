package com.douglei.bpm.process.metadata.node.task.gateway;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class ExclusiveGatewayMetadata extends TaskMetadata {

	public ExclusiveGatewayMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public NodeType getType() {
		return NodeType.EXCLUSIVE_GATEWAY;
	}
}
