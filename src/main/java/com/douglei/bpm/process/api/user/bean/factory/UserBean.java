package com.douglei.bpm.process.api.user.bean.factory;

/**
 * 
 * @author DougLei
 */
public class UserBean {
	protected final String userId; // 用户id, 唯一标识
	public UserBean(String userId) {
		this.userId = userId;
	}
	public final String getUserId() {
		return userId;
	}
	
	@Override
	public String toString() {
		return "User [userId=" + userId + "]";
	}
}
