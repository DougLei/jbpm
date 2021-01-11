package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import java.io.Serializable;

/**
 * 多人办理策略
 * @author DougLei
 */
public class MultiHandlePolicyEntity implements Serializable{
	private boolean serialHandle; // 是否串行办理
	private String serialHandleSequencePolicyName; // 串行办理时的办理顺序策略名称
	
	public MultiHandlePolicyEntity(boolean serialHandle, String serialHandleSequencePolicyName) {
		this.serialHandle = serialHandle;
		this.serialHandleSequencePolicyName = serialHandleSequencePolicyName;
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
}
