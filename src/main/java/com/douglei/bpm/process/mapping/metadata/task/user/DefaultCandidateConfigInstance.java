package com.douglei.bpm.process.mapping.metadata.task.user;

import com.douglei.bpm.process.api.user.task.handle.policy.impl.ClaimByNumberPolicy;
import com.douglei.bpm.process.api.user.task.handle.policy.impl.LastHandleUserDispatchPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.DispatchPolicyEntity;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle.HandlePolicy;

/**
 * 默认的候选人配置实例
 * @author DougLei
 */
public class DefaultCandidateConfigInstance {
	
	/**
	 * 默认的最多可指派的人数表达式: 1人
	 */
	public static final AssignNumber DEFAULT_ASSIGN_NUMBER_1 = new AssignNumber(1, false, false);
	
	/**
	 * 默认的最多可指派的人数表达式: 100%人(所有人)
	 */
	public static final AssignNumber DEFAULT_ASSIGN_NUMBER_100_PERCENT = new AssignNumber(100, true, false);
	
	/**
	 * 默认的认领策略实体: byNumber, 100%
	 */
	public static final ClaimPolicyEntity DEFAULT_CLAIM_POLICY_ENTITY = new ClaimPolicyEntity(ClaimByNumberPolicy.NAME, "100%");
	
	/**
	 * 默认的调度策略实体: 最后办理人进行调度
	 */
	public static final DispatchPolicyEntity DEFAULT_DISPATCH_POLICY_ENTITY = new DispatchPolicyEntity(LastHandleUserDispatchPolicy.NAME);
	
	/**
	 * 默认的办理策略
	 */
	public static final HandlePolicy DEFAULT_HANDLE_POLICY = new HandlePolicy(false, false, DEFAULT_CLAIM_POLICY_ENTITY, null, DEFAULT_DISPATCH_POLICY_ENTITY);
}
