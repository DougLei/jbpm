package com.douglei.bpm.process.mapping.metadata.task.user.candidate;

import com.douglei.bpm.process.mapping.metadata.task.user.DefaultCandidateConfigInstance;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.HandlePolicy;
import com.douglei.orm.mapping.metadata.Metadata;

/**
 * 候选人配置
 * @author DougLei
 */
public class Candidate implements Metadata{
	private AssignPolicy assignPolicy;
	private HandlePolicy handlePolicy;

	public Candidate(AssignPolicy assignPolicy, HandlePolicy handlePolicy) {
		this.assignPolicy = assignPolicy;
		this.handlePolicy = handlePolicy;
	}

	public AssignPolicy getAssignPolicy() {
		return assignPolicy;
	}
	public HandlePolicy getHandlePolicy() {
		if(handlePolicy == null)
			return DefaultCandidateConfigInstance.DEFAULT_HANDLE_POLICY;
		return handlePolicy;
	}
}
