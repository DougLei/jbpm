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
	
	private String reason; // 当前办理人的办理原因, 即为什么办理; 该值会存储在task表的reason列中; 其和 suggest和attitude是互斥的; 用来表示特殊的任务办理, 例如网关的办理, 结束事件的办理, 流程跳转等
	
	private List<UserBean> assignedUsers; // 指派的用户集合
	
	public UserEntity(UserBean handledUser, List<UserBean> assignedUsers) {
		this(handledUser, null, null, null, assignedUsers);
	}
	public UserEntity(UserBean currentHandleUser, String suggest, Attitude attitude, String reason, List<UserBean> assignedUsers) {
		this.currentHandleUser = currentHandleUser;
		this.suggest = suggest;
		this.attitude = attitude;
		this.reason = reason;
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
	 * 获取指派的用户集合
	 * @return
	 */
	public List<UserBean> getAssignedUsers() {
		return assignedUsers;
	}
}
