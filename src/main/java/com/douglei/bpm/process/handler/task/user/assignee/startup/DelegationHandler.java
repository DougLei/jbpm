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
 * 委托信息处理器
 * @author DougLei
 */
public class DelegationHandler {
	private List<DelegationInfo> results; // 最终的委托信息集合
	private DelegationHandler children;

	/**
	 * 
	 * @param taskinstId 任务实例id
	 * @param userId 操作的用户id
	 * @param condition 条件
	 * @throws TaskHandleException
	 */
	public DelegationHandler(String taskinstId, String userId, DelegationSqlCondition condition) throws TaskHandleException{
		this(taskinstId, userId, condition, new HashSet<String>());
	}
	
	// counter: 计数器, 用来防止出现委托无限循环的情况
	private DelegationHandler(String taskinstId, String userId, DelegationSqlCondition condition, HashSet<String> counter) throws TaskHandleException{
		condition.getUserIds().forEach(uid -> {
			if(counter.contains(uid))
				throw new TaskHandleException("递归查询委托信息出现重复的userId=["+uid+"], 相关的任务实例id为["+taskinstId+"], 操作的用户id为["+userId+"]");
			counter.add(uid);
		});
		
		List<DelegationInfo> delegations = SessionContext.getSQLSession().query(DelegationInfo.class, "Delegation", "queryDelegations4Runtime", condition);
		if(delegations.isEmpty())
			return;
		
		this.results = new ArrayList<DelegationInfo>(delegations.size());
		HashSet<String> assigneeUserIds = new HashSet<String>();
		for(DelegationInfo delegation : delegations) {
			if(this.results.contains(delegation))
				continue;
			
			this.results.add(delegation);
			assigneeUserIds.add(delegation.getAssignedUserId());
		}
		
		condition.resetUserIds(assigneeUserIds);
		this.children = new DelegationHandler(taskinstId, userId, condition, counter);
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