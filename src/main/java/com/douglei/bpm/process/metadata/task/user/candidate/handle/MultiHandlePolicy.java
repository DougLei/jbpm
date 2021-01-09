package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import java.io.Serializable;

import com.douglei.bpm.process.metadata.task.user.candidate.DefaultInstance;

/**
 * 多人办理策略
 * @author DougLei
 */
public class MultiHandlePolicy implements Serializable{
	private HandleNumber handleNumber; // 可办理的人数的表达式
	private boolean serialHandle; // 是否串行办理
	private String serialHandleSequencePolicyName; // 串行办理时的办理顺序策略名称
	private String canFinishPolicyName; // (判断)任务是否可以结束的策略名称
	
	public MultiHandlePolicy(HandleNumber handleNumber, boolean serialHandle, String serialHandleSequencePolicyName, String canFinishPolicyName) {
		this.handleNumber = handleNumber;
		this.serialHandle = serialHandle;
		this.serialHandleSequencePolicyName = serialHandleSequencePolicyName;
		this.canFinishPolicyName = canFinishPolicyName;
	}

	/**
	 * 获取可办理的人数的表达式
	 * @return
	 */
	public HandleNumber getHandleNumber() {
		if(handleNumber == null)
			return DefaultInstance.DEFAULT_HANDLE_NUMBER;
		return handleNumber;
	}
	/**
	 * 是否串行办理
	 * @return
	 */
	public boolean isSerialHandle() {
		return serialHandle;
	}
	/**
	 * 获取串行办理时的办理顺序策略名称
	 * @return
	 */
	public String getSerialHandleSequencePolicyName() {
		return serialHandleSequencePolicyName;
	}
	/**
	 * 获取(判断)任务是否可以结束的策略名称
	 * @return
	 */
	public String getCanFinishPolicyName() {
		return canFinishPolicyName;
	}
}
