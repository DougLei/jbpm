package com.douglei.bpm;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.container.ProcessContainerProxy;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionContainer;
import com.douglei.bpm.process.api.user.bean.factory.UserBeanFactory;
import com.douglei.bpm.process.handler.TaskHandleUtil;

/**
 * 流程的bean实例
 * @author DougLei
 */
@Bean
public class ProcessEngineBeans {
	
	@Autowired
	private ProcessContainerProxy processContainer; // 流程容器
	
	@Autowired
	private TaskHandleUtil taskHandleUtil; // 任务办理工具
	
	@Autowired
	private UserBeanFactory userBeanFactory; // 用户bean工厂
	
	@Autowired
	private AssignableUserExpressionContainer assignableUserExpressionContainer; // 可指派的用户表达式容器
	
	public ProcessContainerProxy getProcessContainer() {
		return processContainer;
	}
	public TaskHandleUtil getTaskHandleUtil() {
		return taskHandleUtil;
	}
	public UserBeanFactory getUserBeanFactory() {
		return userBeanFactory;
	}
	public AssignableUserExpressionContainer getAssignableUserExpressionContainer() {
		return assignableUserExpressionContainer;
	}
}
