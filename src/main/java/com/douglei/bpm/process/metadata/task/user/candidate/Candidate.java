package com.douglei.bpm.process.metadata.task.user.candidate;

import java.io.Serializable;

import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;

/**
 * 候选人配置
 * @author DougLei
 */
public class Candidate implements Serializable{
	protected AssignPolicy assignPolicy;
	protected HandlePolicy handlePolicy;

	public Candidate(AssignPolicy assignPolicy, HandlePolicy handlePolicy) {
		this.assignPolicy = assignPolicy;
		this.handlePolicy = handlePolicy;
	}

	public AssignPolicy getAssignPolicy() {
		return assignPolicy;
	}
	public HandlePolicy getHandlePolicy() {
		if(handlePolicy == null)
			return DefaultInstance.DEFAULT_HANDLE_POLICY;
		return handlePolicy;
	}
}
