package com.douglei.bpm.process.handler.task.user.assignee.startup;

/**
 * 
 * @author DougLei
 */
public class AssigneeResult {
	private int assignCount; // 任务指派的人数
	private boolean isAllClaimed; // 任务是否被认领完
	
	/**
	 * 
	 * @param assignCount 任务指派的人数
	 * @param isAllClaimed 任务是否被认领完
	 */
	public AssigneeResult(int assignCount, boolean isAllClaimed) {
		this.assignCount = assignCount;
		this.isAllClaimed = isAllClaimed;
	}
	
	/**
	 * 任务指派的人数
	 * @return
	 */
	public int getAssignCount() {
		return assignCount;
	}
	/**
	 * 任务是否被认领完
	 * @return
	 */
	public boolean isAllClaimed() {
		return isAllClaimed;
	}
}
