package com.douglei.bpm.process.metadata.node.task.event;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class EndEventMetadata extends TaskMetadata {

	public EndEventMetadata(String id, String name, NodeType type) {
		super(id, name, type);
	}
}