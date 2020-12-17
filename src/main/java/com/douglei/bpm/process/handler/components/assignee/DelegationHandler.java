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
	private Map<String, String> map;
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



































































































