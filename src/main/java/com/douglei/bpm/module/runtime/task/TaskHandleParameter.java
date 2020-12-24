package com.douglei.bpm.module.runtime.task;

import java.util.List;

/**
 * 
 * @author DougLei
 */
public class TaskHandleParameter {
	private String userId; // 办理的用户id
	private String suggest; // 办理用户的意见
	private Attitude attitude; // 办理用户的态度
	private List<String> assigneeUserIds; // 指派的用户id数组
	
	public TaskHandleParameter(String userId) {
		this(userId, null, null, null);
	}
	public TaskHandleParameter(String userId, List<String> assigneeUserIds) {
		this(userId, null, null, assigneeUserIds);
	}
	public TaskHandleParameter(String userId, String suggest, List<String> assigneeUserIds) {
		this(userId, suggest, null, assigneeUserIds);
	}
	public TaskHandleParameter(String userId, String suggest, Attitude attitude) {
		this(userId, suggest, attitude, null);
	}
	public TaskHandleParameter(String userId, String suggest, Attitude attitude, List<String> assigneeUserIds) {
		this.userId = userId;
		this.suggest = suggest;
		this.attitude = attitude;
		this.assigneeUserIds = assigneeUserIds;
	}
	
	public String getUserId() {
		return userId;
	}
	public String getSuggest() {
		return suggest;
	}
	public Attitude getAttitude() {
		return attitude;
	}
	public List<String> getAssigneeUserIds() {
		return assigneeUserIds;
	}
}
