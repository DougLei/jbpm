package com.douglei.bpm.process.handler.components.assignee;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author DougLei
 */
public class MultiDelegation {
	private Map<String, Delegation> delegationMap;

	// 添加具体的委托
	public void addDelegation(DelegationInfo delegationInfo) {
		Delegation delegation = delegationMap.get(delegationInfo.getAssigneeId());
		if(delegation == null) {
			delegation = new Delegation();
			delegationMap.put(delegationInfo.getAssigneeId(), delegation);
		}
		delegation.addDetail(delegationInfo.getProcdefCode(), delegationInfo.getProcdefVersion());
	}

	// 是否要委托, 返回委托的用户唯一标识
	public String isDelegate(String processCode, String processVersion) {
		for(Entry<String, Delegation> entry : delegationMap.entrySet()) {
			if(entry.getValue().isDelegate(processCode, processVersion)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
