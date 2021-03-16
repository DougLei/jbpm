package com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle;

import com.douglei.bpm.process.mapping.metadata.task.user.DefaultCandidateConfigInstance;
import com.douglei.orm.mapping.metadata.Metadata;

/**
 * 办理策略
 * @author DougLei
 */
public class HandlePolicy implements Metadata {
	private boolean suggestIsRequired; // 是否需要强制输入意见
	private boolean attitudeIsRequired; // 是否需要强制表态
	private ClaimPolicyEntity claimPolicyEntity; // 认领策略
	private SerialHandlePolicyEntity serialHandlePolicyEntity; // 串行办理策略
	
	public HandlePolicy(boolean suggestIsRequired, boolean attitudeIsRequired, ClaimPolicyEntity claimPolicyEntity, SerialHandlePolicyEntity serialHandlePolicyEntity) {
		this.suggestIsRequired = suggestIsRequired;
		this.attitudeIsRequired = attitudeIsRequired;
		this.claimPolicyEntity = claimPolicyEntity;
		this.serialHandlePolicyEntity = serialHandlePolicyEntity;
	}

	/**
	 * 是否需要强制输入意见
	 * @return
	 */
	public boolean suggestIsRequired() {
		return suggestIsRequired;
	}
	/**
	 * 是否需要强制表态
	 * @return
	 */
	public boolean attitudeIsRequired() {
		return attitudeIsRequired;
	}
	/**
	 * 获取认领策略实体
	 * @return
	 */
	public ClaimPolicyEntity getClaimPolicyEntity() {
		if(claimPolicyEntity == null)
			return DefaultCandidateConfigInstance.DEFAULT_CLAIM_POLICY_ENTITY;
		return claimPolicyEntity;
	}
	/**
	 * 获取串行办理策略
	 * @return
	 */
	public SerialHandlePolicyEntity getSerialHandlePolicyEntity() {
		return serialHandlePolicyEntity;
	}
}
