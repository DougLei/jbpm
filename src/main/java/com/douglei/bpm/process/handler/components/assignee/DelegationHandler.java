package com.douglei.bpm.process.handler.components.assignee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.AssigneeMode;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
class DelegationHandler {
	private Map<String, String> map; // 指派和委托的映射map, <指派的用户id, 委托的用户id>
	private DelegationHandler children;

	public DelegationHandler(List<DelegationInfo> list, DelegationQueryCondition queryCondition, String processCode, String processVersion) {
		if(list.isEmpty())
			return;
		
		// 将查询结果组装成 <指派人id, (多个)具体的委托类实例> 结构
		Map<String, MultiDelegation> delegationInfoMap = new HashMap<String, MultiDelegation>();
		MultiDelegation temp;
		for(DelegationInfo delegationInfo : list) {
			temp = delegationInfoMap.get(delegationInfo.getClientId());
			if(temp == null) {
				temp = new MultiDelegation();
				delegationInfoMap.put(delegationInfo.getClientId(), temp);
			}
			temp.addDelegation(delegationInfo);
		}
		
		// 筛选出需要委托的数据, 并进行递归操作
		List<String> assigneeUserIds = null;
		String assigneeUserId = null;
		for(Entry<String, MultiDelegation> entry : delegationInfoMap.entrySet()) {
			assigneeUserId = entry.getValue().isDelegate(processCode, processVersion);
			if(assigneeUserId != null) {
				if(this.map == null)
					this.map = new HashMap<String, String>();
				this.map.put(entry.getKey(), assigneeUserId);
				
				if(assigneeUserIds == null) 
					assigneeUserIds = new ArrayList<String>(delegationInfoMap.size());
				assigneeUserIds.add(assigneeUserId);
			}
		}
		
		if(this.map != null) { // 证明有委托, 递归去查询是否还有二次委托 
			queryCondition.updateUserIds(assigneeUserIds);
			this.children = new DelegationHandler(SessionContext.getSQLSession().query(DelegationInfo.class, "TaskAssignee", "queryDelegations", queryCondition), queryCondition, processCode, processVersion);
		}
	}

	/**
	 * 添加指派信息
	 * @param taskId
	 * @param parentAssigneeUserId
	 * @param assigneeUserId
	 * @param assigneeList
	 */
	public void addAssignee(int taskId, String parentAssigneeUserId, String assigneeUserId, List<Assignee> assigneeList) {
		Assignee assignee = new Assignee(taskId, assigneeUserId);
		if(parentAssigneeUserId != null) {
			assignee.setParentUserId(parentAssigneeUserId);
			assignee.setModeInstance(AssigneeMode.DELEGATE);
		}
		assigneeList.add(assignee);
		
		if(map == null) // 没有委托
			return;
		String delegationUserId = map.get(assigneeUserId);
		if(delegationUserId == null) // 没有委托
			return;
		
		assignee.setHandleStateInstance(HandleState.INVALID);
		children.addAssignee(taskId, assigneeUserId, delegationUserId, assigneeList);
	}
}


/**
 * 多委托实例, 用来记录一个用户的多个委托
 * @author DougLei
 */
class MultiDelegation {
	private Map<String, Delegation> delegationMap = new HashMap<String, Delegation>();

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

/**
 * 具体的一个委托
 * @author DougLei
 */
class Delegation {
	private List<DelegationProcess> details;
	
	// 添加具体的委托流程
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
}

/**
 * 具体的一个委托流程
 * @author DougLei
 */
class DelegationProcess {
	private String code;
	private String version;
	
	public DelegationProcess(String code, String version) {
		this.code = code;
		this.version = version;
	}
	
	// 是否匹配, 可以委托指定code和version的流程
	public boolean matching(String processCode, String processVersion) {
		if(code.equals(processCode)) {
			if(version == null || version.equals(processVersion))
				return true;
		}
		return false;
	}
}