package com.douglei.bpm;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.configuration.ProcessEngineConfiguration;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionContainer;
import com.douglei.bpm.process.api.user.bean.factory.UserBeanFactory;
import com.douglei.bpm.process.api.user.option.OptionHandlerContainer;
import com.douglei.bpm.process.api.user.task.handle.policy.TaskHandlePolicyContainer;
import com.douglei.bpm.process.handler.TaskHandleUtil;
import com.douglei.bpm.process.mapping.ProcessMappingContainer;

/**
 * 流程(可对外提供)的bean实例
 * @author DougLei
 */
@Bean
public class ProcessEngineBeans {
	
	@Autowired
	private ProcessEngineConfiguration processEngineConfiguration; // 流程引擎配置
	
	@Autowired
	private ProcessMappingContainer processContainer; // 流程容器
	
	@Autowired
	private TaskHandleUtil taskHandleUtil; // 任务办理工具
	
	@Autowired
	private UserBeanFactory userBeanFactory; // 用户bean工厂
	
	@Autowired
	private AssignableUserExpressionContainer assignableUserExpressionContainer; // 可指派的用户表达式容器
	
	@Autowired
	private TaskHandlePolicyContainer taskHandlePolicyContainer; // 任务办理策略容器
	
	@Autowired
	private OptionHandlerContainer optionHandlerContainer; // OptionHandler的容器
	
	// -------------------------------------------------------------------------------------------------
	public ProcessEngineConfiguration getProcessEngineConfiguration() {
		return processEngineConfiguration;
	}
	public ProcessMappingContainer getProcessContainer() {
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
	public TaskHandlePolicyContainer getTaskHandlePolicyContainer() {
		return taskHandlePolicyContainer;
	}
	public OptionHandlerContainer getOptionHandlerContainer() {
		return optionHandlerContainer;
	}
}
