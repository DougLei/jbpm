package com.douglei.bpm.process.api.user.assignable.expression;

/**
 * 表达式在获取具体指派的用户集合时, 需要的参数的类型
 * @author DougLei
 */
public enum ParameterType {
	
	/**
	 * 配置的表达式值, 即流程xml配置文件中, userTask -> candidate -> assignPolicy -> expression里的value属性值
	 */
	EXPRESSION_VALUE,
	
	/**
	 * 配置的表达式扩展值, 即流程xml配置文件中, userTask -> candidate -> assignPolicy -> expression里的extendValue属性值
	 */
	EXPRESSION_EXTEND_VALUE,
	
	/**
	 * 当前办理的用户
	 */
	CURRENT_HANDLE_USER,
	
	/**
	 * 当前处理的任务
	 */
	CURRENT_TASK,
	
	/**
	 * 当前处理的任务的流程变量
	 */
	CURRENT_TASK_VARIABLES;
}
