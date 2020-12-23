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
 * 委托信息处理器
 * @author DougLei
 */
class DelegationHandler {
	private Map<String, Delegation> map; // 指派和委托的映射map, <指派的用户id, 委托的用户id>
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
		Delegation delegation = null;
		for(Entry<String, MultiDelegation> entry : delegationInfoMap.entrySet()) {
			delegation = entry.getValue().isDelegate(processCode, processVersion);
			if(delegation != null) {
				if(this.map == null)
					this.map = new HashMap<String, Delegation>();
				this.map.put(entry.getKey(), delegation);
				
				if(assigneeUserIds == null) 
					assigneeUserIds = new ArrayList<String>(delegationInfoMap.size());
				assigneeUserIds.add(delegation.getUserId());
			}
		}
		
		if(this.map != null) { // 证明有委托, 递归去查询是否还有二次委托 
			queryCondition.updateUserIds(assigneeUserIds);
			this.children = new DelegationHandler(SessionContext.getSQLSession().query(DelegationInfo.class, "Assignee", "queryDelegations", queryCondition), queryCondition, processCode, processVersion);
		}
	}

	/**
	 * 添加指派信息
	 * @param taskinstId
	 * @param groupId
	 * @param parentAssigneeUserId
	 * @param assigneeUserId
	 * @param remark 记录委托的原因
	 * @param assigneeList
	 */
	public void addAssignee(String taskinstId, int groupId, String parentAssigneeUserId, String assigneeUserId, String remark, List<Assignee> assigneeList) {
		Assignee assignee = new Assignee(taskinstId, assigneeUserId, groupId);
		if(parentAssigneeUserId != null) {
			assignee.setParentUserId(parentAssigneeUserId);
			assignee.setRemark(remark);
			assignee.setModeInstance(AssigneeMode.DELEGATE);
		}
		assigneeList.add(assignee);
		
		if(map == null) // 没有委托
			return;
		Delegation delegation = map.get(assigneeUserId);
		if(delegation == null) // 没有委托
			return;
		
		assignee.setHandleStateInstance(HandleState.INVALID);
		children.addAssignee(taskinstId, groupId, assigneeUserId, delegation.getUserId(), delegation.getRemark(), assigneeList);
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
			delegation = new Delegation(delegationInfo.getAssigneeId(), delegationInfo.getReason());
			delegationMap.put(delegationInfo.getAssigneeId(), delegation);
		}
		delegation.addDetail(delegationInfo.getProcdefCode(), delegationInfo.getProcdefVersion());
	}

	// 是否要委托, 返回委托的用户id
	public Delegation isDelegate(String processCode, String processVersion) {
		for(Delegation delegation : delegationMap.values()) {
			if(delegation.isDelegate(processCode, processVersion)) {
				return delegation;
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
	private String userId;
	private String remark;
	private List<DelegationProcess> details;
	
	public Delegation(String userId, String remark) {
		this.userId = userId;
		this.remark = remark;
	}
	public String getUserId() {
		return userId;
	}
	public String getRemark() {
		return remark;
	}

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