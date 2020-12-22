package com.douglei.bpm.process.handler.components.assignee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.orm.context.SessionContext;

/**
 * (运行)指派信息处理器
 * @author DougLei
 */
public class AssigneeHandler {
	private Assigners assigners;
	private DelegationHandler delegationHandler;
	
	public AssigneeHandler(String code, String version, Assigners assigners) {
		this.assigners = assigners;
		
		DelegationQueryCondition queryCondition = new DelegationQueryCondition(new Date().getTime(), assigners);
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
		List<Assignee> assigneeList = new ArrayList<Assignee>(assigners.size() + 5); // +5是备用的长度
		assigners.getList().forEach(assigner -> delegationHandler.addAssignee(taskinstId, UUID.randomUUID().toString(), null, assigner.getUserId(), null, assigneeList));
		return assigneeList;
	}
}
