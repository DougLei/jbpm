package com.douglei.bpm.module.runtime.task.command.parameter;

import java.util.HashSet;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class GeneralTaskParameter {
	private String userId; // 办理人id
	private HashSet<String> assignedUserIds; // 下一环节的办理人id集合
	
	/**
	 * 设置办理人id
	 * @param userId
	 * @return
	 */
	public GeneralTaskParameter setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	/**
	 * 添加下一环节的办理人id
	 * @param assignedUserId
	 * @return
	 */
	public GeneralTaskParameter addAssignedUserId(String assignedUserId) {
		if(assignedUserIds == null)
			assignedUserIds = new HashSet<String>();
		assignedUserIds.add(assignedUserId);
		return this;
	}
	
	/**
	 * 添加下一环节的办理人id集合
	 * @param assignedUserIds
	 * @return
	 */
	public GeneralTaskParameter addAssignedUserIds(List<String> assignedUserIds) {
		if(this.assignedUserIds == null) 
			this.assignedUserIds = new HashSet<String>(assignedUserIds);
		else 
			this.assignedUserIds.addAll(assignedUserIds);
		return this;
	}
	
	/**
	 * 获取办理人id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 获取下一环节的办理人id集合
	 * @return
	 */
	public HashSet<String> getAssignedUserIds() {
		return assignedUserIds;
	}
}
