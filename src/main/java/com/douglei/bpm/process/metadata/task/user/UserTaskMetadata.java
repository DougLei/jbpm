package com.douglei.bpm.process.metadata.task.user;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.api.listener.Listener;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TimeLimit;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.Option;

/**
 * 
 * @author DougLei
 */
public class UserTaskMetadata extends TaskMetadata {
	private String pageID;
	private TimeLimit timeLimit;
	private Candidate candidate;
	private List<Option> options;
	
	public UserTaskMetadata(String id, String name, String defaultOutputFlowId, String pageID, TimeLimit timeLimit, Candidate candidate, List<Option> options) {
		super(id, name, defaultOutputFlowId);
		this.pageID = pageID;
		this.timeLimit = timeLimit;
		this.candidate = candidate;
		this.options = options;
	}
	
	public Candidate getCandidate() {
		return candidate;
	}
	public Option getOption(String type) {
		if(options == null)
			return null;
		
		for (Option option : options) {
			if(option.getType().equals(type))
				return option;
		}
		return null;
	}
	
	@Override
	public String getPageID() {
		return pageID;
	}
	@Override
	public TimeLimit getTimeLimit() {
		return timeLimit;
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