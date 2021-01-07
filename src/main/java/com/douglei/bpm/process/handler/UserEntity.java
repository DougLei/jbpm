package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;

/**
 * 
 * @author DougLei
 */
public class UserEntity {
	private UserBean currentHandleUser; // 当前办理的用户
	private String suggest; // 当前办理用户的意见
	private Attitude attitude; // 当前办理用户的态度
	private List<UserBean> assignedUsers; // 指派的用户集合
	
	public UserEntity(UserBean handledUser) {
		this(handledUser, null, null, null);
	}
	public UserEntity(UserBean handledUser, String suggest) {
		this(handledUser, suggest, null, null);
	}
	public UserEntity(UserBean handledUser, List<UserBean> assignedUsers) {
		this(handledUser, null, null, assignedUsers);
	}
	public UserEntity(UserBean handledUser, String suggest, Attitude attitude) {
		this(handledUser, suggest, attitude, null);
	}
	public UserEntity(UserBean currentHandleUser, String suggest, Attitude attitude, List<UserBean> assignedUsers) {
		this.currentHandleUser = currentHandleUser;
		this.suggest = suggest;
		this.attitude = attitude;
		this.assignedUsers = (assignedUsers==null)?new ArrayList<UserBean>():assignedUsers;
	}
	
	/**
	 * 获取当前办理的用户
	 * @return
	 */
	public UserBean getCurrentHandleUser() {
		return currentHandleUser;
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
	public String getAttitude() {
		if(attitude == null)
			return null;
		return attitude.name();
	}
	/**
	 * 获取指派的用户集合
	 * @return
	 */
	public List<UserBean> getAssignedUsers() {
		return assignedUsers;
	}
}
