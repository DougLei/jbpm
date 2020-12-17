package com.douglei.bpm.process.handler.components.assignee;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
class Delegation {
	private String assigneeUserId;
	private List<DelegationProcess> details;
	
	public Delegation(String assigneeUserId) {
		this.assigneeUserId = assigneeUserId;
	}
	public void addDetail(String processCode, String processVersion) {
		if(processCode == null)
			return;
		if(details == null)
			details = new ArrayList<DelegationProcess>(5);
		details.add(new DelegationProcess(processCode, processVersion));
	}
	
	// 是否要委托
	public boolean isDelegate(String processCode, String processVersion) {
		if(details == null)
			return true;
		for (DelegationProcess process : details) {
			if(process.matching(processCode, processVersion))
				return true;
		}
		return false;
	}
	public String getAssigneeUserId() {
		return assigneeUserId;
	}
}
