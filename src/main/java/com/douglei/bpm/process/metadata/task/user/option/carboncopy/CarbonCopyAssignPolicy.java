package com.douglei.bpm.process.metadata.task.user.option.carboncopy;

import com.douglei.bpm.process.metadata.task.user.DefaultCandidateConfigInstance;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;

/**
 * 
 * @author DougLei
 */
public class CarbonCopyAssignPolicy extends AssignPolicy{
	
	public CarbonCopyAssignPolicy(boolean isDynamic, AssignNumber assignNumber) {
		super(isDynamic, assignNumber);
	}
	
	@Override
	public AssignNumber getAssignNumber() {
		if(assignNumber == null)
			return DefaultCandidateConfigInstance.DEFAULT_ASSIGN_NUMBER_100_PERCENT;
		return assignNumber;
	}
}
