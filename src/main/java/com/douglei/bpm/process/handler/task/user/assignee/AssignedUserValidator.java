package com.douglei.bpm.process.handler.task.user.assignee;

import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;

/**
 * 
 * @author DougLei
 */
public class AssignedUserValidator {
	private UserTaskMetadata taskMetadata; // 办理的用户任务元数据
	private GeneralHandleParameter handleParameter; // 办理的参数 
	
	public AssignedUserValidator(UserTaskMetadata taskMetadata, GeneralHandleParameter handleParameter) {
		this.taskMetadata = taskMetadata;
		this.handleParameter = handleParameter;
	}

	/**
	 * 验证指派的用户
	 * @throws TaskHandleException
	 */
	public void validate() throws TaskHandleException{
		// 如果是指派模式, 那就看指派的信息符不符合要求, 有没有超过最多允许指派的人数等等
		
		
		// 如果是另外两种模式, 直接去获取自己想要的值
		
		if(taskMetadata.getCandidate().getAssignPolicy().isDynamic()) {
			validate4Dynamic();
		}else {
			validate4Static();
		}
		
		if(handleParameter.getUserEntity().getAssignedUsers().isEmpty())
			throw new TaskHandleException("[id="+taskMetadata.getId()+", name="+taskMetadata.getName()+", isDynamicAssign="+taskMetadata.getCandidate().getAssignPolicy().isDynamic()+"]的UserTask未指派具体的办理人员");
	}

	// *************************************************
	// 验证动态指派的人
	// *************************************************
	private void validate4Dynamic() {
		// TODO 
		
		
		
		
	}
	
	// *************************************************
	// 验证静态指派的人
	// *************************************************
	private void validate4Static() {
		if(handleParameter.getUserEntity().getAssignedUsers().size() > 0) // 忽略已经指派的用户
			handleParameter.getUserEntity().getAssignedUsers().clear();
		
		
		
		
	}
}
