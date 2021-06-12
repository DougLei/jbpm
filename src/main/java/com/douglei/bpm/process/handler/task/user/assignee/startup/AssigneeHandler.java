package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.handler.AbstractHandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.mapping.metadata.listener.ActiveTime;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class AssigneeHandler {
	protected UserTaskMetadata userTaskMetadata;
	protected AbstractHandleParameter handleParameter;
	protected ProcessEngineBeans processEngineBeans;
	
	/**
	 * 设置指派信息处理器参数
	 * @param userTaskMetadata
	 * @param handleParameter
	 * @param processEngineBeans
	 * @return
	 */
	public final AssigneeHandler setParameters(UserTaskMetadata userTaskMetadata, AbstractHandleParameter handleParameter, ProcessEngineBeans processEngineBeans) {
		this.userTaskMetadata = userTaskMetadata;
		this.handleParameter = handleParameter;
		this.processEngineBeans = processEngineBeans;
		return this;
	}
	
	/**
	 * 获取指派策略
	 * @return
	 */
	protected AssignPolicy getAssignPolicy() {
		return userTaskMetadata.getCandidate().getAssignPolicy();
	}
	
	/**
	 * 验证指派的用户id集合
	 * @param assignPolicy
	 * @return
	 */
	protected HashSet<String> validate(AssignPolicy assignPolicy) {
		// 获取实际指派的用户id集合
		HashSet<String> assignedUserIds = handleParameter.getUserEntity().getAssignEntity().getAssignedUserIds();
		
		// 验证: 根据指派策略, 进行基础验证和处理
		if(assignPolicy.isDynamic()) {
			if(assignedUserIds.isEmpty())
				throw new TaskHandleException("任务未指派办理人员");
			if(!assignPolicy.getAssignNumber().isPercent() && assignedUserIds.size() > assignPolicy.getAssignNumber().getNumber())
				throw new TaskHandleException("任务实际指派的人数["+assignedUserIds.size()+"]超过配置的上限["+assignPolicy.getAssignNumber().getNumber()+"]");
		}
		
		// 获取可指派的用户id集合
		HashSet<String> assignableUserIds = processEngineBeans.getTaskHandleUtil().getAssignableUserIds(
				handleParameter.getProcessInstanceId(), 
				handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(), 
				handleParameter.getUserEntity().getUserId(), assignPolicy);
		if(assignableUserIds.isEmpty())
			throw new TaskHandleException("任务不存在可指派的办理人员");
		
		// 根据指派策略, 验证实际指派的用户id集合和可指派的用户id集合
		if(!assignPolicy.isDynamic()) 
			return assignableUserIds;
			
		processEngineBeans.getTaskHandleUtil().validateAssignedUsers(assignedUserIds, assignableUserIds, assignPolicy.getAssignNumber());
		return assignedUserIds;
	}
	
	/**
	 * 获取指派信息集合
	 * @param assignPolicy
	 * @param assignedUserIds
	 * @return
	 */
	protected List<Assignee> getAssigneeList(AssignPolicy assignPolicy, HashSet<String> assignedUserIds){
		List<Assignee> assigneeList = new ArrayList<Assignee>(assignedUserIds.size());
		
		String taskinstId= handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId();
		DelegateHandler delegateHandler = new DelegateHandler(
				taskinstId, handleParameter.getUserEntity().getUserId(), 
				new DelegationSqlCondition(handleParameter.getProcessMetadata().getCode(), handleParameter.getProcessMetadata().getVersion(), assignedUserIds));
		
		boolean isFixedAssign = !assignPolicy.isDynamic(); // 是否固定指派
		int groupId = 1;
		for (String assignedUserId : assignedUserIds) 
			delegateHandler.addAssignee(taskinstId, groupId++, 0, assignedUserId, null, isFixedAssign , assigneeList);
		return assigneeList;
	}
	
	/**
	 * 尝试自动认领
	 * @param assignedUserIds
	 * @param assigneeList
	 * @return
	 */
	protected boolean tryAutoClaim(HashSet<String> assignedUserIds, List<Assignee> assigneeList) {
		ClaimPolicyEntity entity = userTaskMetadata.getCandidate().getHandlePolicy().getClaimPolicyEntity();
		ClaimPolicy policy = processEngineBeans.getAPIContainer().getClaimPolicy(entity.getName());
		return policy.tryAutoClaim(entity.getValue(), assignedUserIds.size(), assigneeList, handleParameter.getCurrentDate());
	}
	
	/**
	 * 进行指派信息处理
	 * @return
	 * @throws TaskHandleException
	 */
	public final AssigneeResult execute() throws TaskHandleException {
		// 验证并获取最终指派的用户id集合
		HashSet<String> assignedUserIds = validate(getAssignPolicy());
		if(assignedUserIds != handleParameter.getUserEntity().getAssignEntity().getAssignedUserIds())
			handleParameter.getUserEntity().getAssignEntity().setAssignedUserIds(assignedUserIds);
		
		// 获取指派信息集合
		List<Assignee> assigneeList = getAssigneeList(getAssignPolicy(), assignedUserIds);
		
		// 尝试进行自动认领
		boolean isAllClaimed = tryAutoClaim(assignedUserIds, assigneeList);
		
		// 保存指派信息, 触发TASK_ASSIGNED时机的Listener, 并返回指派结果
		SessionContext.getTableSession().save(assigneeList);
		processEngineBeans.getTaskHandleUtil().notifyListners(userTaskMetadata, handleParameter, ActiveTime.TASK_ASSIGNED);
		return new AssigneeResult(assignedUserIds.size(), isAllClaimed);
	}
}
