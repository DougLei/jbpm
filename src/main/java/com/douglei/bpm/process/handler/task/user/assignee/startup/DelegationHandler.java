package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.douglei.bpm.module.execution.task.AssignMode;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;


/**
 * 委托信息处理器
 * @author DougLei
 */
public class DelegationHandler {
	private Map<String, Delegation> resultMap; // 指派和委托的映射map, <指派的用户id, 委托的用户id>
	private DelegationHandler children;

	/**
	 * 
	 * @param processCode 流程定义的code
	 * @param processVersion 流程定义的version
	 * @param condition 条件
	 * @param taskinstId 任务实例id
	 * @param userId 操作的用户id
	 * @throws TaskHandleException
	 */
	public DelegationHandler(String processCode, String processVersion, SqlCondition condition, String taskinstId, String userId) throws TaskHandleException{
		this(processCode, processVersion, condition, taskinstId, userId, new HashSet<String>());
	}
	
	// counter: 计数器, 用来防止出现委托死循环的情况
	private DelegationHandler(String processCode, String processVersion, SqlCondition condition, String taskinstId, String userId, HashSet<String> counter) throws TaskHandleException{
		condition.getUserIds().forEach(uid -> {
			if(counter.contains(uid))
				throw new TaskHandleException("递归查询委托信息出现重复的userId=["+uid+"], 相关的任务实例id为["+taskinstId+"], 操作的用户id为["+userId+"]");
			counter.add(uid);
		});
		
		List<DelegationInfo> list = SessionContext.getSQLSession().query(DelegationInfo.class, "Assignee", "queryDelegations", condition);
		if(list.isEmpty())
			return;
		
		// 将查询结果组装成 <发起委托的人id, (多个)具体的委托类实例> 结构
		Map<String, MultiDelegation> delegationInfoMap = new HashMap<String, MultiDelegation>();
		MultiDelegation temp;
		for(DelegationInfo delegationInfo : list) {
			temp = delegationInfoMap.get(delegationInfo.getUserId());
			if(temp == null) {
				temp = new MultiDelegation();
				delegationInfoMap.put(delegationInfo.getUserId(), temp);
			}
			temp.addDelegation(delegationInfo);
		}
		
		// 筛选出需要委托的数据, 并进行递归操作
		HashSet<String> assigneeUserIds = null;
		Delegation delegation = null;
		for(Entry<String, MultiDelegation> entry : delegationInfoMap.entrySet()) {
			delegation = entry.getValue().isDelegate(processCode, processVersion);
			if(delegation != null) {
				if(this.resultMap == null)
					this.resultMap = new HashMap<String, Delegation>();
				this.resultMap.put(entry.getKey(), delegation);
				
				if(assigneeUserIds == null) 
					assigneeUserIds = new HashSet<String>(delegationInfoMap.size());
				assigneeUserIds.add(delegation.getUserId());
			}
		}
		
		if(this.resultMap != null) { // 证明当前层的人员有委托, 递归去查询是否还有二次委托 
			condition.updateUserIds(assigneeUserIds);
			this.children = new DelegationHandler(processCode, processVersion, condition, taskinstId, userId, counter);
		}
	}
	

	/**
	 * 添加指派信息
	 * @param taskinstId
	 * @param groupId
	 * @param chainId
	 * @param assigneeUserId
	 * @param reason 记录委托的原因
	 * @param isFixedAssign 是否是固定指派
	 * @param assigneeList
	 */
	public void addAssignee(String taskinstId, int groupId, int chainId, String assigneeUserId, String reason, boolean isFixedAssign, List<Assignee> assigneeList) {
		Assignee assignee = new Assignee(taskinstId, assigneeUserId, groupId, chainId);
		if(isFixedAssign) {
			assignee.setModeInstance(AssignMode.FIXED);
		} else if(chainId > 0) {
			assignee.setReason(reason);
			assignee.setModeInstance(AssignMode.DELEGATED);
		}
		assigneeList.add(assignee);
		
		if(resultMap == null) { // 都没有委托
			assignee.setIsChainLast(1);
			return;
		}
		Delegation delegation = resultMap.get(assigneeUserId);
		if(delegation == null) { // 当前assigneeUserId没有委托
			assignee.setIsChainLast(1);
			return;
		}
		
		assignee.setHandleStateInstance(HandleState.COMPETITIVE_UNCLAIM);
		children.addAssignee(taskinstId, groupId, chainId+1, delegation.getUserId(), delegation.getReason(), false, assigneeList);
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
		Delegation delegation = delegationMap.get(delegationInfo.getAssignedUserId());
		if(delegation == null) {
			delegation = new Delegation(delegationInfo.getAssignedUserId(), delegationInfo.getReason());
			delegationMap.put(delegationInfo.getAssignedUserId(), delegation);
		}
		delegation.addDetail(delegationInfo.getProcdefCode(), delegationInfo.getProcdefVersion());
	}

	// 是否要委托, 返回具体的委托实例
	public Delegation isDelegate(String processCode, String processVersion) {
		for(Delegation delegation : delegationMap.values()) {
			if(delegation.isDelegate(processCode, processVersion)) 
				return delegation;
		}
		return null;
	}
}

/**
 * 具体的一个委托
 * @author DougLei
 */
class Delegation {
	private String userId; // 被委托的用户id
	private String reason;
	private List<DelegationProcess> details; // 具体委托的流程集合
	
	public Delegation(String userId, String reason) {
		this.userId = userId;
		this.reason = reason;
	}
	public String getUserId() {
		return userId;
	}
	public String getReason() {
		return reason;
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