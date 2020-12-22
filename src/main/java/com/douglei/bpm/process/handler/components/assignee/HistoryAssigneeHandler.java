package com.douglei.bpm.process.handler.components.assignee;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.douglei.bpm.module.history.task.HistoryAssignee;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.orm.context.SessionContext;

/**
 * (历史)指派信息处理器
 * @author DougLei
 */
public class HistoryAssigneeHandler {
	private String taskinstId;
	private HistoryAssigneeQueryCondition queryCondition;
	
	public HistoryAssigneeHandler(String taskinstId) {
		this.taskinstId = taskinstId;
		this.queryCondition = new HistoryAssigneeQueryCondition(taskinstId);
	}

	/**
	 * 获取任务的历史指派信息集合
	 * @return
	 */
	public List<HistoryAssignee> getHistoryAssigneeList() {
		List<HistoryAssignee> historyAssigneeList = SessionContext.getSqlSession().query(HistoryAssignee.class, "select * from bpm_ru_assignee where taskinst_id=? and handle_state=?", Arrays.asList(taskinstId, HandleState.FINISHED.name()));
		if(historyAssigneeList.isEmpty())
			return Collections.emptyList();
		
		addParentAssignee(historyAssigneeList);
		return historyAssigneeList;
	}

	// (递归)添加上一层的指派信息集合
	private void addParentAssignee(List<HistoryAssignee> historyAssigneeList) {
		queryCondition.resetParentUserIds();
		for (HistoryAssignee historyAssignee : historyAssigneeList) {
			if(historyAssignee.getParentUserId() != null) 
				queryCondition.addParentUserId(historyAssignee.getParentUserId());
		}
		
		List<HistoryAssignee> historyParentAssigneeList = SessionContext.getSQLSession().query(HistoryAssignee.class, "Assignee", "queryAssigneeListByParentUserIds", queryCondition);
		if(historyParentAssigneeList.isEmpty())
			return;
		
		addParentAssignee(historyParentAssigneeList);
		historyAssigneeList.addAll(historyParentAssigneeList);
	}
}
