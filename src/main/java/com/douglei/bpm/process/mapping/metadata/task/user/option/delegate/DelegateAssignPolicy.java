package com.douglei.bpm.process.mapping.metadata.task.user.option.delegate;

import com.douglei.bpm.process.mapping.metadata.task.user.DefaultCandidateConfigInstance;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;

/**
 * 
 * @author DougLei
 */
public class DelegateAssignPolicy extends AssignPolicy{
	
	public DelegateAssignPolicy() {
		super(true, null);
	}
	
	@Override
	public AssignNumber getAssignNumber() {
		return DefaultCandidateConfigInstance.DEFAULT_ASSIGN_NUMBER_1;
	}
}
