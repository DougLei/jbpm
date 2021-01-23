package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import java.io.Serializable;

import com.douglei.bpm.process.metadata.task.user.DefaultCandidateConfigInstance;

/**
 * 办理策略
 * @author DougLei
 */
public class HandlePolicy implements Serializable {
	private boolean suggestIsRequired; // 是否需要强制输入意见
	private boolean attitudeIsRequired; // 是否需要强制表态
	private ClaimPolicyEntity claimPolicyEntity; // 认领策略
	private MultiHandlePolicyEntity multiHandlePolicyEntity; // 多人办理策略
	
	public HandlePolicy(boolean suggestIsRequired, boolean attitudeIsRequired, ClaimPolicyEntity claimPolicyEntity, MultiHandlePolicyEntity multiHandlePolicyEntity) {
		this.suggestIsRequired = suggestIsRequired;
		this.attitudeIsRequired = attitudeIsRequired;
		this.claimPolicyEntity = claimPolicyEntity;
		this.multiHandlePolicyEntity = multiHandlePolicyEntity;
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
	 * 是否多人办理
	 * @return
	 */
	public boolean isMultiHandle() {
		return multiHandlePolicyEntity != null;
	}
	/**
	 * 获取多人办理策略
	 * @return
	 */
	public MultiHandlePolicyEntity getMultiHandlePolicyEntity() {
		return multiHandlePolicyEntity;
	}
}
