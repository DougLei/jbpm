package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.HashSet;
import java.util.List;

import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;

/**
 * 
 * @author DougLei
 */
public class AssigneeHandler4Backsteps extends AssigneeHandler{

	@Override
	protected HashSet<String> validate(AssignPolicy assignPolicy) {
		return handleParameter.getUserEntity().getAssignEntity().getAssignedUserIds();
	}

	@Override
	protected boolean tryAutoClaim(HashSet<String> assignedUserIds, List<Assignee> assigneeList) {
		assigneeList.stream().filter(assignee -> assignee.isChainLast()).forEach(assignee -> assignee.claim(handleParameter.getCurrentDate()));
		return true;
	}
}
