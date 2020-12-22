package com.douglei.bpm.process.handler.components.assignee;

/**
 * 指派人信息类
 * @author DougLei
 */
public class Assigner {
	protected final String userId;
	public Assigner(String userId) {
		this.userId = userId;
	}
	public final String getUserId() {
		return userId;
	}
	
	@Override
	public String toString() {
		return "Assigner [userId=" + userId + "]";
	}
}
