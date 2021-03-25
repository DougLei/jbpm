package com.douglei.bpm.process.api.user.assignable.expression;

/**
 * 表达式在获取具体可指派的用户集合时, 需要的参数
 * @author DougLei
 */
public class AssignableUserExpressionParameter {
	private String procinstId; // 当前操作的流程实例id
	private String taskinstId; // 当前操作的任务实例id
	private String currentHandleUserId; // 当前办理的用户id
	
	/**
	 * 
	 * @param procinstId 当前操作的流程实例id
	 * @param taskinstId 当前操作的任务实例id
	 * @param currentHandleUserId 当前办理的用户id
	 */
	public AssignableUserExpressionParameter(String procinstId, String taskinstId, String currentHandleUserId) {
		this.procinstId = procinstId;
		this.taskinstId = taskinstId;
		this.currentHandleUserId = currentHandleUserId;
	}

	/**
	 * 获取当前操作的流程实例id
	 * @return
	 */
	public String getProcinstId() {
		return procinstId;
	}
	/**
	 * 获取当前操作的任务实例id
	 * @return
	 */
	public String getTaskinstId() {
		return taskinstId;
	}
	/**
	 * 获取当前办理的用户id
	 * @return
	 */
	public String getCurrentHandleUserId() {
		return currentHandleUserId;
	}
}
