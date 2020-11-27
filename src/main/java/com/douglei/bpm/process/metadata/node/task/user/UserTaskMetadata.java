package com.douglei.bpm.process.metadata.node.task.user;

import java.util.List;

import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.TaskMetadata;
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
	
	
	protected UserTaskMetadata(String id, String name, NodeType type) {
		super(id, name, type);
	}
}