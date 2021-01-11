package com.douglei.bpm.process.metadata.task.user.candidate;

import com.douglei.bpm.process.api.user.task.handle.policy.ClaimPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.ClaimPolicyEntity;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;

/**
 * 默认的实例
 * @author DougLei
 */
public class DefaultInstance {
	
	/**
	 * 默认的最多可指派的人数表达式
	 */
	public static final AssignNumber DEFAULT_ASSIGN_NUMBER = new AssignNumber(1, false, false);
	
	/**
	 * 默认的认领策略实体
	 */
	public static final ClaimPolicyEntity DEFAULT_CLAIM_POLICY_ENTITY = new ClaimPolicyEntity(ClaimPolicy.DEFAULT_POLICY_NAME, null);
	
	/**
	 * 默认的办理策略
	 */
	public static final HandlePolicy DEFAULT_HANDLE_POLICY = new HandlePolicy(false, false, DEFAULT_CLAIM_POLICY_ENTITY, null);
}
