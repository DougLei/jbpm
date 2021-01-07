package com.douglei.bpm.process.api.user.assignable.expression;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.handler.VariableEntities;

/**
 * 表达式在获取具体指派的用户集合时, 需要的参数
 * @author DougLei
 */
public class Parameter {
	private String expressionValue;
	private String expressionExtendValue;
	private UserBean currentHandleUser;
	private Task currentTask;
	private VariableEntities currentTaskVariables;
	
	/**
	 * 获取配置的表达式值, 即流程xml配置文件中, userTask -> candidate -> assignPolicy -> expression里的value属性值
	 * @return
	 */
	public String getExpressionValue() {
		return expressionValue;
	}
	
	/**
	 * 获取配置的表达式扩展值, 即流程xml配置文件中, userTask -> candidate -> assignPolicy -> expression里的extendValue属性值
	 * @return
	 */
	public String getExpressionExtendValue() {
		return expressionExtendValue;
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
