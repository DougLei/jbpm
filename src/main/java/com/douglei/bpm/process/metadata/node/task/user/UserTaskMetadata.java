package com.douglei.bpm.process.metadata.node.task.user;

import java.util.List;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.TaskNodeMetadata;
import com.douglei.bpm.process.metadata.node.task.user.option.Option;

/**
 * 
 * @author DougLei
 */
public class UserTaskMetadata extends TaskNodeMetadata {
	private String pageID;
	private String timeLimitExpr;
	
	private Candidate candidate;
	private List<Option> options;
	
	public UserTaskMetadata(String id, String name) {
		super(id, name);
	}

	@Override
	public NodeType getType() {
		return NodeType.USER_TASK;
	}
}