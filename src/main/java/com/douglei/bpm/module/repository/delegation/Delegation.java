package com.douglei.bpm.module.repository.delegation;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class Delegation extends ProcessDelegation{
	private List<DelegationDetail> details;
	
	public List<DelegationDetail> getDetails() {
		return details;
	}
	public void setDetails(List<DelegationDetail> details) {
		this.details = details;
	}
	public void addDetail(DelegationDetail detail) {
		if(details == null)
			details = new ArrayList<DelegationDetail>();
		details.add(detail);
	}
}
