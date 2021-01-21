package com.douglei.bpm.module.runtime.task.command.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class GeneralTaskParameter {
	private String userId; // 办理人id
	private List<String> assignUserIds; // 下一环节的办理人id集合
	
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
	 * @param assignUserId
	 * @return
	 */
	public GeneralTaskParameter addAssignUserId(String assignUserId) {
		if(assignUserIds == null)
			assignUserIds = new ArrayList<String>();
		if(assignUserIds.isEmpty() || !assignUserIds.contains(assignUserId))
			assignUserIds.add(assignUserId);
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
	public List<String> getAssignUserIds() {
		return assignUserIds;
	}
}
