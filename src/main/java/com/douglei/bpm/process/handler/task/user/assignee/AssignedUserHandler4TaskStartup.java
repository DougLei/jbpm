package com.douglei.bpm.process.handler.task.user.assignee;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.orm.context.SessionContext;

/**
 * 任务启动时, 指派的用户处理器
 * @author DougLei
 */
public class AssignedUserHandler4TaskStartup {
	private List<UserBean> assignedUsers;
	private DelegationHandler delegationHandler;
	
	public AssignedUserHandler4TaskStartup(String code, String version, List<UserBean> assignedUsers) {
		this.assignedUsers = assignedUsers;
		
		DelegationSqlQueryCondition queryCondition = new DelegationSqlQueryCondition(assignedUsers);
		this.delegationHandler = new DelegationHandler(
				SessionContext.getSQLSession().query(DelegationInfo.class, "Assignee", "queryDelegations", queryCondition), 
				queryCondition,
				code, 
				version);
	}
	
	/**
	 * 保存任务的指派信息集合
	 * @param taskinstId
	 */
	public void saveAssigneeList(String taskinstId) {
		List<Assignee> assigneeList = new ArrayList<Assignee>(assignedUsers.size() + 5); // +5是备用的长度
		int groupId = 1;
		for (UserBean assignedUser : assignedUsers) 
			delegationHandler.addAssignee(taskinstId, groupId++, null, assignedUser.getUserId(), null, assigneeList);
		
		SessionContext.getTableSession().save(assigneeList);
	}
}
