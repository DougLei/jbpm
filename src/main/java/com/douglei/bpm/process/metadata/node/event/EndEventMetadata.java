package com.douglei.bpm.process.metadata.node.event;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.TaskNodeMetadata;

/**
 * 
 * @author DougLei
 */
public class EndEventMetadata extends TaskNodeMetadata {

	public EndEventMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public NodeType getType() {
		return NodeType.END_EVENT;
	}
}
