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
	public final int hashCode() {
		return userId.hashCode();
	}
	
	@Override
	public final boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(this == obj)
			return true;
		return userId.equals(((UserBean) obj).userId);
	}
	
	@Override
	public String toString() {
		return "User [userId=" + userId + "]";
	}
}
