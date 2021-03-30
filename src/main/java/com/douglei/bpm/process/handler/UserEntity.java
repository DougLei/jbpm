package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.douglei.bpm.module.history.task.Attitude;

/**
 * 
 * @author DougLei
 */
public class UserEntity {
	private String currentHandleUserId; // 当前办理的用户id
	
	private String suggest; // 当前办理用户的意见
	private Attitude attitude; // 当前办理用户的态度
	
	private String reason; // 当前办理人的办理原因, 即为什么办理; 该值会存储在task表的reason列中; 其和 suggest和attitude是互斥的; 用来表示特殊的任务办理, 例如网关的办理, 结束事件的办理, 流程跳转等
	
	private HashSet<String> assignedUserIds; // 指派的用户id集合
	
	public UserEntity(String currentHandleUserId, HashSet<String> assignedUserIds) {
		this(currentHandleUserId, null, null, null, assignedUserIds);
	}
	public UserEntity(String currentHandleUserId, String suggest, Attitude attitude, String reason, HashSet<String> assignedUserIds) {
		this.currentHandleUserId = currentHandleUserId;
		this.suggest = suggest;
		this.attitude = attitude;
		this.reason = reason;
		this.assignedUserIds = assignedUserIds;
	}
	
	/**
	 * 获取当前办理的用户id
	 * @return
	 */
	public String getCurrentHandleUserId() {
		return currentHandleUserId;
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
	 * 追加指派的用户id集合
	 * @param userIds
	 */
	public void appendAssignedUserIds(HashSet<String> userIds) {
		if(assignedUserIds == null)
			assignedUserIds = userIds;
		else
			assignedUserIds.addAll(userIds);
	}
	/**
	 * 获取指派的用户id集合
	 * @return
	 */
	public List<String> getAssignedUserIds() {
		return new ArrayList<String>(assignedUserIds);
	}
}
