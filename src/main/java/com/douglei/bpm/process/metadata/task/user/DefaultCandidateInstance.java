package com.douglei.bpm.process.metadata.task.user;

import com.douglei.bpm.process.api.user.task.handle.policy.impl.ClaimByNumberPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;

/**
 * 默认的候选人配置
 * @author DougLei
 */
public class DefaultCandidateInstance {
	
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
	public static final ClaimPolicyEntity DEFAULT_CLAIM_POLICY_ENTITY = new ClaimPolicyEntity(ClaimByNumberPolicy.POLICY_NAME, "100%");
	
	/**
	 * 默认的办理策略
	 */
	public static final HandlePolicy DEFAULT_HANDLE_POLICY = new HandlePolicy(false, false, DEFAULT_CLAIM_POLICY_ENTITY, null);
}
