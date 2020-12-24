package com.douglei.bpm.process.handler;

/**
 * 
 * @author DougLei
 */
public class User {
	protected final String userId;
	public User(String userId) {
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
