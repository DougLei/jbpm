package com.douglei.bpm.process.metadata.task.user;

import java.util.List;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.listener.Listener;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.Option;

/**
 * 
 * @author DougLei
 */
public class UserTaskMetadata extends TaskMetadata {
	private String pageID;
	private TimeLimitExpression timeLimitExpression;
	private Candidate candidate;
	private List<Option> options;
	private List<Listener> listeners;
	
	public UserTaskMetadata(String id, String name, String defaultOutputFlowId, String pageID, String timeLimit) {
		super(id, name, defaultOutputFlowId);
		this.pageID = pageID;
	}
	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public TimeLimitExpression getTimeLimitExpression() {
		return timeLimitExpression;
	}
	public Candidate getCandidate() {
		return candidate;
	}
	public List<Option> getOptions() {
		return options;
	}
	public List<Listener> getListeners() {
		return listeners;
	}
	
	@Override
	public String getPageID() {
		return pageID;
	}
	@Override
	public boolean requiredUserHandle() {
		return true;
	}
	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}