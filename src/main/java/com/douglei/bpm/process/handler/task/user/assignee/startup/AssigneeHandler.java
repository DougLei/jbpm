package com.douglei.bpm.process.handler.task.user.assignee.startup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.orm.context.SessionContext;

/**
 * 启动任务时, 指派信息处理器
 * @author DougLei
 */
public class AssigneeHandler {
	private String code;
	private String version;
	private List<UserBean> assignedUsers; // 实际指派的人员集合
	
	/**
	 * 
	 * @param code 任务所属流程定义的code
	 * @param version 任务所属流程定义的version
	 * @param assignedUsers 上一环节实际指派的用户集合
	 */
	public AssigneeHandler(String code, String version, List<UserBean> assignedUsers) {
		this.code = code;
		this.version = version;
		this.assignedUsers = assignedUsers;
	}
	
	/**
	 * 对指派的用户集合进行预处理
	 * <p>
	 * 主要包括指派用户的验证, 以及静态指派用户的获取
	 * @param currentUserTaskMetadata 当前用户任务的元数据实例
	 * @param handleParameter
	 * @param processEngineBeans
	 * @throws TaskHandleException
	 */
	public void pretreatment(UserTaskMetadata currentUserTaskMetadata, HandleParameter handleParameter, ProcessEngineBeans processEngineBeans) throws TaskHandleException{
		// 根据指派策略, 进行基础验证和处理
		AssignPolicy assignPolicy = currentUserTaskMetadata.getCandidate().getAssignPolicy();
		if(assignPolicy.isDynamic()) {
			if(assignedUsers.isEmpty())
				throw new TaskHandleException("任务未指派办理人员");
			if(!assignPolicy.getAssignNumber().isPercent() && assignedUsers.size() > assignPolicy.getAssignNumber().getNumber())
				throw new TaskHandleException("任务实际指派的人数["+assignedUsers.size()+"]超过配置的上限["+assignPolicy.getAssignNumber().getNumber()+"]");
		}
		
		// 获取具体可指派的所有用户集合
		List<UserBean> assignableUsers = processEngineBeans.getTaskHandleUtil().getAssignableUsers(assignPolicy, currentUserTaskMetadata, handleParameter);
		if(assignableUsers.isEmpty())
			throw new TaskHandleException("任务不存在可指派的办理人员");
		
		// 根据指派策略, 可指派的用户集合, 和实际指派的用户集合, 进行验证
		if(assignPolicy.isDynamic()) {
			processEngineBeans.getTaskHandleUtil().validateAssignedUsers(assignedUsers, assignableUsers, assignPolicy.getAssignNumber());
		}else {
			// 静态指派, 忽略动态指派的用户
			if(assignedUsers.size() > 0)
				assignedUsers.clear();
			assignedUsers.addAll(assignableUsers);
		}
	}
	
	/**
	 * 保存指派信息
	 * @param userId 当前操作的userId
	 * @param currentUserTaskMetadata 当前(启动的)用户任务的元数据实例
	 * @param currentTask 当前的任务实例
	 * @param currentDate
	 * @throws TaskHandleException
	 */
	public void save(String userId, UserTaskMetadata currentUserTaskMetadata, Task currentTask, Date currentDate) throws TaskHandleException {
		if(assignedUsers.isEmpty())
			throw new TaskHandleException("任务未指派办理人员");
		
		// 查询指派用户的委托数据, 将处理后的指派数据保存到运行表
		List<String> assignedUserIds = new ArrayList<String>(assignedUsers.size());
		assignedUsers.forEach(user -> assignedUserIds.add(user.getUserId()));
		
		DelegationHandler delegationHandler = new DelegationHandler(
				code, 
				version,
				new SqlCondition(assignedUserIds), 
				currentTask.getTaskinstId(),
				userId);
		
		boolean isStaticAssign = !currentUserTaskMetadata.getCandidate().getAssignPolicy().isDynamic(); // 是否静态指派
		List<Assignee> assigneeList = new ArrayList<Assignee>(assignedUsers.size()*2);
		int groupId = 1;
		for (UserBean assignedUser : assignedUsers) 
			delegationHandler.addAssignee(currentTask.getTaskinstId(), groupId++, 0, assignedUser.getUserId(), null, isStaticAssign , assigneeList);
		
		// 之前任务记录指派的人数, 不等于当前实际指派的人数, 进行更新
		if(currentTask.getAssignCount() != assignedUsers.size()) 
			currentTask.setAssignCount(assignedUsers.size());
			
		// 如果只指派了一个人, 则直接进行认领操作
		if(currentTask.getAssignCount() == 1) {
			Assignee lastAssignee = assigneeList.get(assigneeList.size()-1);
			lastAssignee.claim(currentDate);
			
			currentTask.setDispatchRight(lastAssignee.getUserId());
			currentTask.setAllClaimed();
		}
		
		SessionContext.getTableSession().save(assigneeList);
	}
	
	/**
	 * 获取实际指派的人数
	 * @return
	 */
	public int getAssignCount() {
		return assignedUsers.size();
	}
}
