package com.douglei.bpm.process.handler.task.user.assignee;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.module.history.task.HistoryAssignee;
import com.douglei.bpm.module.runtime.task.AssignMode;
import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.orm.context.SessionContext;

/**
 * 办理任务时, 指派的用户处理器
 * @author DougLei
 */
public class AssignedUserHandler4Handling {
	private List<HistoryAssignee> independentAssigneeList; // 独立的指派信息集合, 从构造函数的参数assigneeList中提取出来; 指parentUserId=null, 或者mode=ASSISTED的指派信息实例
	private List<HistoryAssignee> assigneeList; // 指派信息集合
	private AssigneeSqlCondition assigneeSqlCondition; // 指派信息的sql条件
	
	public AssignedUserHandler4Handling(HandleParameter handleParameter, List<HistoryAssignee> assigneeList) {
		// 设置独立的指派信息集合
		HistoryAssignee historyAssignee = null;
		for(int i=0;i < assigneeList.size();i++) {
			historyAssignee = assigneeList.get(i);
			historyAssignee.setSuggest(handleParameter.getUserEntity().getSuggest());
			historyAssignee.setAttitude(handleParameter.getUserEntity().getAttitude());
			historyAssignee.setFinishTime(handleParameter.getCurrentDate());
			historyAssignee.setHandleStateInstance(HandleState.FINISHED);
			
			if(historyAssignee.getParentUserId() == null || historyAssignee.getModeInstance() == AssignMode.ASSISTED) {
				if(independentAssigneeList == null)
					independentAssigneeList = new ArrayList<HistoryAssignee>(assigneeList.size());
				independentAssigneeList.add(historyAssignee);
				assigneeList.remove(i--);
			}
		}
		
		if(assigneeList.isEmpty())
			return;
		
		// 追加parent指派信息集合
		this.assigneeSqlCondition = new AssigneeSqlCondition(handleParameter.getTask().getTaskinstId(), assigneeList);
		appendParentAssigneeList(assigneeList);
		this.assigneeList = assigneeList;
	}

	// 追加parent指派信息集合
	private void appendParentAssigneeList(List<HistoryAssignee> assigneeList) {
		List<HistoryAssignee> parentAssigneeList = SessionContext.getSQLSession().query(HistoryAssignee.class, "Assignee", "queryParentAssigneeList", assigneeSqlCondition);
		if(parentAssigneeList.isEmpty()) 
			return;
		
		assigneeList.addAll(parentAssigneeList); // 追加操作
		
		for(int i=0;i<parentAssigneeList.size();i++) {
			if(parentAssigneeList.get(i).getParentUserId() == null)
				parentAssigneeList.remove(i--);
		}
		if(parentAssigneeList.isEmpty())
			return;
		
		assigneeSqlCondition.updateAssigneeList(parentAssigneeList);
		appendParentAssigneeList(assigneeList);
	}

	/**
	 * 调度指派信息
	 */
	public void assigneeDispatch() {
		// 1. 获取独立的历史指派信息集合, 从运行表删除, 并保存到历史表
		if(independentAssigneeList != null) {
			SessionContext.getSQLSession().executeUpdate("Assignee", "deleteAssigneeByIds", independentAssigneeList);
			SessionContext.getTableSession().save(independentAssigneeList);
		}
		
		// 2. 获取历史指派信息集合, 从运行表删除, 并保存到历史表
		if(assigneeList != null) {
			SessionContext.getSQLSession().executeUpdate("Assignee", "deleteAssigneeByGroupIds", assigneeSqlCondition);
			SessionContext.getTableSession().save(assigneeList);
		}
	}
}
