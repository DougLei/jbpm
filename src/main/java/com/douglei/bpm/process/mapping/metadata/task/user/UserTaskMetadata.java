package com.douglei.bpm.process.mapping.metadata.task.user;

import java.util.List;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public class UserTaskMetadata extends TaskMetadata {
	private String pageID;
	private TimeLimit timeLimit;
	private Candidate candidate;
	private List<Option> options;
	
	public UserTaskMetadata(String id, String name, String defaultOutputFlowId, String pageID) {
		super(id, name, defaultOutputFlowId);
		this.pageID = StringUtil.isEmpty(pageID)?null:pageID;
	}
	public void setTimeLimit(TimeLimit timeLimit) {
		this.timeLimit = timeLimit;
	}
	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public Candidate getCandidate() {
		return candidate;
	}
	public List<Option> getOptions() {
		return options;
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
	public Type getType() {
		return Type.USER_TASK;
	}
}