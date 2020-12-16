package com.douglei.bpm.module.runtime.task.assignee;

/**
 * 
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
	public final boolean equals(Object obj) {
		return userId.equals(((Assigner) obj).userId);
	}
}
