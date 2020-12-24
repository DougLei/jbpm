package com.douglei.bpm.process.handler.task.user.assignee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.handler.User;
import com.douglei.orm.context.SessionContext;

/**
 * 指派的用户处理器
 * @author DougLei
 */
public class AssignedUserHandler {
	private List<User> assignedUsers;
	private DelegationHandler delegationHandler;
	
	public AssignedUserHandler(String code, String version, List<User> assignedUsers) {
		this.assignedUsers = assignedUsers;
		
		DelegationQueryCondition queryCondition = new DelegationQueryCondition(new Date().getTime(), assignedUsers);
		this.delegationHandler = new DelegationHandler(
				SessionContext.getSQLSession().query(DelegationInfo.class, "Assignee", "queryDelegations", queryCondition), 
				queryCondition,
				code, version);
	}
	
	/**
	 * 获取任务的指派信息集合
	 * @param taskinstId
	 * @return
	 */
	public List<Assignee> getAssigneeList(String taskinstId) {
		List<Assignee> assigneeList = new ArrayList<Assignee>(assignedUsers.size() + 5); // +5是备用的长度
		int groupId = 0;
		for (User assignedUser : assignedUsers) 
			delegationHandler.addAssignee(taskinstId, groupId++, null, assignedUser.getUserId(), null, assigneeList);
		return assigneeList;
	}
}
