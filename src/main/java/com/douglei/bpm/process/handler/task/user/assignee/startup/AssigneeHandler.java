package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class AssigneeHandler {
	
	/**
	 * 验证并保存指派信息
	 * @param currentUserTaskMetadata
	 * @param currentTask
	 * @param handleParameter
	 * @param processEngineBeans
	 * @throws TaskHandleException
	 */
	public void execute(UserTaskMetadata currentUserTaskMetadata, Task currentTask, HandleParameter handleParameter, ProcessEngineBeans processEngineBeans) throws TaskHandleException {
		// 获取实际指派的用户id集合
		List<String> assignedUserIds = handleParameter.getUserEntity().getAssignedUserIds(); 
		
		// 验证: 根据指派策略, 进行基础验证和处理
		AssignPolicy assignPolicy = currentUserTaskMetadata.getCandidate().getAssignPolicy();
		if(assignPolicy.isDynamic()) {
			if(assignedUserIds.isEmpty())
				throw new TaskHandleException("任务未指派办理人员");
			if(!assignPolicy.getAssignNumber().isPercent() && assignedUserIds.size() > assignPolicy.getAssignNumber().getNumber())
				throw new TaskHandleException("任务实际指派的人数["+assignedUserIds.size()+"]超过配置的上限["+assignPolicy.getAssignNumber().getNumber()+"]");
		}
		
		// 获取具体可指派的人员集合
		List<String> assignableUserIds = processEngineBeans.getTaskHandleUtil().getAssignableUserIds(
				currentTask.getProcinstId(), currentTask.getTaskinstId(), handleParameter.getUserEntity().getCurrentHandleUserId(), assignPolicy);
		if(assignableUserIds.isEmpty())
			throw new TaskHandleException("任务不存在可指派的办理人员");
		
		// 根据指派策略, 可指派的人员集合, 和实际指派的人员集合, 进行验证
		if(assignPolicy.isDynamic()) {
			processEngineBeans.getTaskHandleUtil().validateAssignedUsers(assignedUserIds, assignableUserIds, assignPolicy.getAssignNumber());
		}else {
			// 静态指派, 忽略动态指派的用户
			if(assignedUserIds.size() > 0)
				assignedUserIds.clear();
			assignedUserIds.addAll(assignableUserIds);
		}
		
		// 保存: 查询指派用户的委托数据, 将处理后的指派数据保存到运行表
		DelegationHandler delegationHandler = new DelegationHandler(
				handleParameter.getProcessMetadata().getCode(), 
				handleParameter.getProcessMetadata().getVersion(),
				new SqlCondition(assignedUserIds), 
				currentTask.getTaskinstId(),
				handleParameter.getUserEntity().getCurrentHandleUserId());
		
		boolean isFixedAssign = !currentUserTaskMetadata.getCandidate().getAssignPolicy().isDynamic(); // 是否固定指派
		List<Assignee> assigneeList = new ArrayList<Assignee>(assignedUserIds.size());
		int groupId = 1;
		for (String assignedUserId : assignedUserIds) 
			delegationHandler.addAssignee(currentTask.getTaskinstId(), groupId++, 0, assignedUserId, null, isFixedAssign , assigneeList);
		
		// 记录指派的人数
		currentTask.setAssignCount(assignedUserIds.size());	
		
		// 如果只指派了一个人, 则进行认领操作
		if(currentTask.getAssignCount() == 1) {
			assigneeList.get(assigneeList.size()-1).claim(handleParameter.getCurrentDate());
			currentTask.setIsAllClaimed(1);
		}
		
		SessionContext.getTableSession().save(assigneeList);
	}
}
