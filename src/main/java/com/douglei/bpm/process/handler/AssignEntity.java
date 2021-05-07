package com.douglei.bpm.process.handler;

import java.util.HashSet;

import com.douglei.bpm.process.handler.task.user.assignee.startup.AssigneeHandler;

/**
 * 
 * @author DougLei
 */
public class AssignEntity {
	private HashSet<String> assignedUserIds; // 指派的用户id集合
	private AssigneeHandler assigneeHandler; // 指派信息处理器
	
	/**
	 * 添加指派的用户id
	 * @param userId
	 */
	public void addAssignedUserId(String userId) {
		if(assignedUserIds == null)
			assignedUserIds = new HashSet<String>();
		assignedUserIds.add(userId);
	}
	/**
	 * 添加指派的用户id集合
	 * @param userIds
	 */
	public void addAssignedUserIds(HashSet<String> userIds) {
		if(assignedUserIds == null)
			assignedUserIds = userIds;
		else
			assignedUserIds.addAll(userIds);
	}
	/**
	 * 设置指派的用户id集合(可用来重置)
	 * @param assignedUserIds2
	 */
	public void setAssignedUserIds(HashSet<String> userIds) {
		this.assignedUserIds = userIds;
	}
	/**
	 * 设置指派信息处理器(可用来重置)
	 * @param assigneeHandler
	 */
	public void setAssigneeHandler(AssigneeHandler assigneeHandler) {
		this.assigneeHandler = assigneeHandler;
	}
	
	/**
	 * 获取指派的用户id集合
	 * @return
	 */
	public HashSet<String> getAssignedUserIds() {
		if(assignedUserIds == null)
			assignedUserIds= new HashSet<String>();
		return assignedUserIds;
	}
	/**
	 * 获取指派信息处理器
	 * @return
	 */
	public AssigneeHandler getAssigneeHandler() {
		if(assigneeHandler == null)
			assigneeHandler = new AssigneeHandler();
		return assigneeHandler;
	}
}
