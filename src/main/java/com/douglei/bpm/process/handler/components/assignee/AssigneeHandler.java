package com.douglei.bpm.process.handler.components.assignee;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.ExecuteParameter;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class AssigneeHandler {
	private int taskId;
	private Assigners assigners;
	private DelegationHandler delegationHandler;
	
	public AssigneeHandler(Task currentTask, ExecuteParameter parameter) {
		this.taskId = currentTask.getId();
		this.assigners = parameter.getAssigners();
		
		DelegationQueryCondition queryCondition = new DelegationQueryCondition(currentTask.getStartTime().getTime(), assigners);
		this.delegationHandler = new DelegationHandler(
				SessionContext.getSQLSession().query(DelegationInfo.class, "TaskAssignee", "queryDelegations", queryCondition), 
				queryCondition,
				parameter.getProcessMetadata().getCode(),
				parameter.getProcessMetadata().getVersion());
	}
	
	/**
	 * 获取最终的指派信息集合
	 * @return
	 */
	public List<Assignee> getAssigneeList() {
		List<Assignee> assigneeList = new ArrayList<Assignee>(assigners.size() + 5); // +5是备用的长度
		assigners.getList().forEach(assigner -> delegationHandler.addAssignee(taskId, null, assigner.getUserId(), null, assigneeList));
		return assigneeList;
	}
}
