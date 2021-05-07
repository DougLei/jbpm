package com.douglei.bpm.process.handler;

import com.douglei.bpm.module.execution.task.history.Attitude;

/**
 * 
 * @author DougLei
 */
public class UserEntity {
	private String userId; // 当前办理的用户id
	private String suggest; // 当前办理用户的意见
	private Attitude attitude; // 当前办理用户的态度
	private String reason; // 当前办理人的办理原因, 即为什么办理; 该值会存储在task表的reason列中; 其和 suggest和attitude是互斥的; 用来表示特殊的任务办理, 例如网关的办理, 结束事件的办理, 流程跳转等
	private AssignEntity assignEntity; // 指派实体
	
	public UserEntity(String userId, String suggest, Attitude attitude, String reason, AssignEntity assignEntity) {
		this.userId = userId;
		this.suggest = suggest;
		this.attitude = attitude;
		this.reason = reason;
		this.assignEntity = assignEntity;
	}

	/**
	 * 获取当前办理的用户id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 获取当前办理用户的意见
	 * @return
	 */
	public String getSuggest() {
		return suggest;
	}
	/**
	 * 获取当前办理用户的态度
	 * @return
	 */
	public Attitude getAttitude() {
		return attitude;
	}
	/**
	 * 获取当前办理人的办理原因
	 * @return
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * 获取指派实体
	 * @return
	 */
	public AssignEntity getAssignEntity() {
		return assignEntity;
	}
}
