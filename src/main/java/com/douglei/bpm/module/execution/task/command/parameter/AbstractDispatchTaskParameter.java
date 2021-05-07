package com.douglei.bpm.module.execution.task.command.parameter;

import com.douglei.bpm.process.handler.AssignEntity;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractDispatchTaskParameter {
	private String userId; // 办理人id
	private AssignEntity assignEntity = new AssignEntity(); // 指派实体
	
	protected AbstractDispatchTaskParameter(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取办理人id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 获取指派实体
	 * @return
	 */
	public AssignEntity getAssignEntity() {
		return assignEntity;
	}
}
