package com.douglei.bpm.process.handler.task.user.assignee.startup;

import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.option.dispatch.JumpOption;

/**
 * 
 * @author DougLei
 */
public class AssigneeHandler4Jump extends AssigneeHandler {
	private JumpOption option;

	public AssigneeHandler4Jump(JumpOption option) {
		this.option = option;
	}

	@Override
	protected AssignPolicy getAssignPolicy() {
		if(option.getCandidate() != null)
			return option.getCandidate().getAssignPolicy();
		return super.getAssignPolicy();
	}
}
