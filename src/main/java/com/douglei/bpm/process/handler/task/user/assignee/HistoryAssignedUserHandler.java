package com.douglei.bpm.process.handler.task.user.assignee;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.history.task.HistoryAssignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.orm.context.SessionContext;

/**
 * (历史)指派的用户处理器
 * @author DougLei
 */
public class HistoryAssignedUserHandler {
	private String taskinstId;
	private HistoryAssigneeQueryCondition queryCondition;
	
	public HistoryAssignedUserHandler(String taskinstId) {
		this.taskinstId = taskinstId;
		this.queryCondition = new HistoryAssigneeQueryCondition(taskinstId);
	}

	/**
	 * 获取任务的历史指派信息集合
	 * @return
	 */
	public List<HistoryAssignee> getHistoryAssigneeList() {
		List<HistoryAssignee> historyAssigneeList = SessionContext.getSqlSession()
				.query(HistoryAssignee.class, "select * from bpm_ru_assignee where taskinst_id=? and handle_state=?", Arrays.asList(taskinstId, HandleState.FINISHED.name()));
		if(historyAssigneeList.isEmpty())
			throw new TaskHandleException("查询历史指派信息失败, taskinst_id为["+taskinstId+"]的任务, 没有查询到任何有效的指派信息");
		
		addParentAssigneeList(historyAssigneeList);
		return historyAssigneeList;
	}

	// (递归)添加上一层的指派信息集合
	private void addParentAssigneeList(List<HistoryAssignee> historyAssigneeList) {
		queryCondition.resetParentUserIds();
		for (HistoryAssignee historyAssignee : historyAssigneeList) {
			if(historyAssignee.getParentUserId() != null) 
				queryCondition.addParentUserId(historyAssignee.getParentUserId());
		}
		if(queryCondition.isEmpty())
			return;
		
		List<HistoryAssignee> parentHistoryAssigneeList = SessionContext.getSQLSession().query(HistoryAssignee.class, "Assignee", "queryAssigneeListByParentUserIds", queryCondition);
		if(parentHistoryAssigneeList.isEmpty())
			return;
		
		addParentAssigneeList(parentHistoryAssigneeList);
		historyAssigneeList.addAll(parentHistoryAssigneeList);
	}
}
