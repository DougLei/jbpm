package com.douglei.bpm.process.metadata.node.task.user;

import java.util.List;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.MultiFlowTaskMetadata;
import com.douglei.bpm.process.metadata.node.task.user.option.Option;

/**
 * 
 * @author DougLei
 */
public class UserTaskMetadata extends MultiFlowTaskMetadata {
	private String pageID;
	private String timeLimitExpr;
	
	private Candidate candidate;
	private List<Option> options;
	
	public UserTaskMetadata(String id, String name, String defaultFlowId, String pageID) {
		super(id, name, defaultFlowId);
		this.pageID = pageID;
	}
	
	@Override
	public String getPageID() {
		return pageID;
	}
	
	@Override
	public boolean supportUserHandling() {
		return true;
	}
	
	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}