package com.douglei.bpm.process.metadata.node.task.user;

import java.util.List;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.TaskMetadata;
import com.douglei.bpm.process.metadata.node.task.user.option.Option;

/**
 * 
 * @author DougLei
 */
public class UserTaskMetadata extends TaskMetadata {
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