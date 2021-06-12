package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.douglei.bpm.module.execution.task.AssignMode;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class DelegateHandler {
	private List<DelegationInfo> results; // 最终的委托信息集合
	private DelegateHandler parent;
	private DelegateHandler children;

	/**
	 * 
	 * @param taskinstId 任务实例id
	 * @param userId 操作的用户id
	 * @param condition 条件
	 * @throws TaskHandleException
	 */
	public DelegateHandler(String taskinstId, String userId, DelegationSqlCondition condition) throws TaskHandleException{
		this(null, taskinstId, userId, condition, new HashSet<String>());
	}
	
	// counter: 计数器, 防止委托无限循环的情况
	private DelegateHandler(DelegateHandler parent, String taskinstId, String userId, DelegationSqlCondition condition, HashSet<String> counter) throws TaskHandleException{
		this.parent = parent;
		condition.getUserIds().forEach(uid -> counter.add(uid));
		
		List<DelegationInfo> delegations = SessionContext.getSQLSession().query(DelegationInfo.class, "Delegation", "queryDelegations4Runtime", condition);
		if(delegations.isEmpty())
			return;
		
		// 记录具体的委托信息集合
		this.results = new ArrayList<DelegationInfo>(delegations.size());
		for(DelegationInfo delegation : delegations) {
			if(delegation.getUserId().equals(delegation.getAssignedUserId()) || this.results.contains(delegation))
				continue;
			this.results.add(delegation);
		}
		
		// 如果存在委托信息, 验证是否有委托无限循环的情况
		if(this.results.size()> 0) {
			HashSet<String> assigneeUserIds = condition.resetUserIds();
			for(int i=0; i<results.size(); i++) {
				if(counter.contains(results.get(i).getAssignedUserId())) {
					DelegationInfo info = results.remove(i--);
					parent.giveupDelegate(info.getAssignedUserId(), info.getUserId());
					continue;
				}
				assigneeUserIds.add(results.get(i).getAssignedUserId());
			}
			
			// 验证后依然存在合法的委托信息, 则继续进行递归查询
			if(assigneeUserIds.size() > 0)
				this.children = new DelegateHandler(this, taskinstId, userId, condition, counter);
		}
	}
	
	/**
	 * 放弃委托(递归); 即出现委托无限循环的情况时, 撤销所有委托, 直接指派给委托发起人
	 * @param userId 委托发起人id
	 * @param currentAssigneeUserId 当前被委托人id
	 */
	private void giveupDelegate(String userId, String currentAssigneeUserId) {
		for(int i=0; i<results.size(); i++) {
			if(results.get(i).getUserId().equals(userId)) {
				results.remove(i);
				return;
			}else if(results.get(i).getAssignedUserId().equals(currentAssigneeUserId)) {
				DelegationInfo info = results.remove(i);
				parent.giveupDelegate(userId, info.getUserId());
				return;
			}
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
		
		// 判断当前的assigneeUserId是否存在委托
		if(results != null) {
			for(DelegationInfo delegation : results) {
				if(delegation.getUserId().equals(assigneeUserId)) {
					// 有委托, 进行递归操作
					assignee.setHandleStateInstance(HandleState.COMPETITIVE_UNCLAIM);
					children.addAssignee(taskinstId, groupId, chainId+1, delegation.getAssignedUserId(), delegation.getReason(), false, assigneeList);
					return;
				}
			}
		}
		
		// 没有委托, 为末级指派信息
		assignee.setIsChainLast(1);
	}
}