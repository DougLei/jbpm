package com.douglei.bpm.process.api.user.assignable.expression;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;

/**
 * 表达式在获取具体可指派的用户集合时, 需要的参数
 * @author DougLei
 */
public class AssignableUserExpressionParameter {
	private UserBean currentHandleUser;
	private Task currentTask;
	private VariableEntities currentTaskVariables;
	
	/**
	 * 
	 * @param metadata
	 * @param handleParameter
	 * @param processEngineBeans
	 */
	public AssignableUserExpressionParameter(UserTaskMetadata metadata, HandleParameter handleParameter, ProcessEngineBeans processEngineBeans) {
		// TODO Auto-generated constructor stub
		
		
		
	}

	/**
	 * 获取当前办理的用户
	 * @return
	 */
	public UserBean getCurrentHandleUser() {
		return currentHandleUser;
	}
	
	/**
	 * 获取当前处理的任务
	 * @return
	 */
	public Task getCurrentTask() {
		return currentTask;
	}
	
	/**
	 * 获取当前处理的任务的流程变量
	 * @return
	 */
	public VariableEntities getCurrentTaskVariables() {
		return currentTaskVariables;
	}
}
