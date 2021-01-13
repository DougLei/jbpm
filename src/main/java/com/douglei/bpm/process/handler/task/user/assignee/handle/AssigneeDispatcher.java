package com.douglei.bpm.process.handler.task.user.assignee.handle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.module.history.task.HistoryAssignee;
import com.douglei.bpm.module.runtime.task.AssignMode;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.orm.context.SessionContext;

/**
 *  办理任务时, 指派信息调度器
 * @author DougLei
 */
public class AssigneeDispatcher {
	private String taskinstId;
	private List<HistoryAssignee> independentAssigneeList; // 独立的指派信息集合, 从构造函数中, 名为assigneeList的变量提取出来; 指chainId=0, 或者mode=ASSISTED的指派信息实例
	private List<HistoryAssignee> assigneeList; // 指派信息集合
	
	/**
	 * 
	 * @param taskinstId 任务实例id
	 * @param handleUserId 当前办理的用户id
	 * @param suggest 当前办理的用户意见
	 * @param attitude 当前办理的用户态度
	 * @param currentDate 当前办理的时间
	 */
	public AssigneeDispatcher(String taskinstId, String handleUserId, String suggest, Attitude attitude, Date currentDate) {
		this.taskinstId = taskinstId;
		
		// 查询当前办理的用户, 在当前任务中认领的指派信息集合
		List<HistoryAssignee> assigneeList = SessionContext.getSqlSession().query(
				HistoryAssignee.class, 
				"select * from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
				Arrays.asList(taskinstId, handleUserId, HandleState.CLAIMED.name()));
		
		// 设置独立的指派信息集合
		HistoryAssignee historyAssignee = null;
		for(int i=0;i < assigneeList.size();i++) {
			historyAssignee = assigneeList.get(i);
			historyAssignee.finish(attitude, suggest, currentDate);
			
			if(historyAssignee.isChainFirst() || historyAssignee.getModeInstance() == AssignMode.ASSISTED) {
				if(independentAssigneeList == null)
					independentAssigneeList = new ArrayList<HistoryAssignee>(assigneeList.size());
				independentAssigneeList.add(historyAssignee);
				assigneeList.remove(i--);
			}
		}
		
		if(assigneeList.isEmpty())
			return;
		
		// 追加parent指派信息集合
		appendParentAssigneeList(assigneeList);
		this.assigneeList = assigneeList;
	}

	// 追加parent指派信息集合
	private static final String QUERY_PARENT_ASSIGNEE_LIST_SQL = "select * from bpm_ru_assignee where taskinst_id=? and group_id=? and chain_id<?";
	private void appendParentAssigneeList(List<HistoryAssignee> assigneeList) {
		List<Object> sqlParameters = new ArrayList<Object>(3);
		sqlParameters.add(taskinstId);
		sqlParameters.add(null);
		sqlParameters.add(null);
		
		List<HistoryAssignee> tempList = null;
		for (HistoryAssignee assignee : assigneeList) {
			sqlParameters.set(1, assignee.getGroupId());
			sqlParameters.set(2, assignee.getChainId());
			
			tempList = SessionContext.getSqlSession().query(HistoryAssignee.class, QUERY_PARENT_ASSIGNEE_LIST_SQL, sqlParameters);
			if(tempList.size() > 0)
				assigneeList.addAll(tempList);
		}
	}
	
	/**
	 * 调度当前办理用户的指派信息
	 * <p>
	 * 从运行表移动到历史表
	 */
	public void dispatch() {
		// 1. 获取独立的历史指派信息集合, 从运行表删除, 并保存到历史表
		if(independentAssigneeList != null) {
			SessionContext.getSQLSession().executeUpdate("Assignee", "deleteAssigneeByIds", independentAssigneeList);
			SessionContext.getTableSession().save(independentAssigneeList);
		}
		
		// 2. 获取历史指派信息集合, 从运行表删除, 并保存到历史表
		if(assigneeList != null) {
			SessionContext.getSQLSession().executeUpdate("Assignee", "deleteAssigneeByGroupIds", assigneeList);
			SessionContext.getTableSession().save(assigneeList);
		}
	}

	/**
	 * 调度当前任务的所有指派信息
	 * <p>
	 * 将多余的指派信息直接从运行表删除
	 */
	public void dispatchAll() {
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_assignee where taskinst_id=?", Arrays.asList(taskinstId));
	}
}
