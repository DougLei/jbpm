package com.douglei.bpm.process.metadata.task.user;

import java.util.List;

import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.api.listener.Listener;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TimeLimit;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.OptionMetadata;

/**
 * 
 * @author DougLei
 */
public class UserTaskMetadata extends TaskMetadata {
	private String pageID;
	private TimeLimit timeLimit;
	private Candidate candidate;
	private List<OptionMetadata> optionEntities;
	private List<Listener> listeners;
	
	public UserTaskMetadata(String id, String name, String defaultOutputFlowId, String pageID, TimeLimit timeLimit, Candidate candidate) {
		super(id, name, defaultOutputFlowId);
		this.pageID = pageID;
		this.timeLimit = timeLimit;
		this.candidate = candidate;
	}

	public Candidate getCandidate() {
		return candidate;
	}
	public OptionMetadata getOptionEntity(String optionType) {
		if(optionEntities == null)
			return null;
		
		for (OptionMetadata optionEntity : optionEntities) {
			if(optionEntity.getType().equals(optionType))
				return optionEntity;
		}
		return null;
	}
	public List<Listener> getListeners() {
		return listeners;
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