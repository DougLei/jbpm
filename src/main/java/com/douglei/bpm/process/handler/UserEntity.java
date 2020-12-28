package com.douglei.bpm.process.handler;

import java.util.List;

import com.douglei.bpm.module.history.task.Attitude;

/**
 * 
 * @author DougLei
 */
public class UserEntity {
	private User handledUser; // 办理的用户实例
	private String suggest; // 办理用户的意见
	private Attitude attitude; // 办理用户的态度
	private List<User> assignedUsers; // 被指派的用户集合
	
	public UserEntity(User handledUser) {
		this(handledUser, null, null, null);
	}
	public UserEntity(User handledUser, String suggest) {
		this(handledUser, suggest, null, null);
	}
	public UserEntity(User handledUser, List<User> assignedUsers) {
		this(handledUser, null, null, assignedUsers);
	}
	public UserEntity(User handledUser, String suggest, Attitude attitude) {
		this(handledUser, suggest, attitude, null);
	}
	public UserEntity(User handledUser, String suggest, Attitude attitude, List<User> assignedUsers) {
		this.handledUser = handledUser;
		this.suggest = suggest;
		this.attitude = attitude;
		this.assignedUsers = assignedUsers;
	}
	
	public String getSuggest() {
		return suggest;
	}
	public String getAttitude() {
		if(attitude == null)
			return null;
		return attitude.name();
	}
	public User getHandledUser() {
		return handledUser;
	}
	public List<User> getAssignedUsers() {
		return assignedUsers;
	}
}
