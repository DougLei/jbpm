package com.douglei.bpm.bean;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.handler.TaskHandlerUtil;
import com.douglei.bpm.process.handler.UserFactory;
import com.douglei.bpm.process.handler.VariableScheduler;

/**
 * 
 * @author DougLei
 */
@Bean
public class BeanInstances {
	@Autowired
	private ProcessContainerProxy processContainer; // 流程容器
	@Autowired
	private TaskHandlerUtil taskHandlerUtil; // 任务办理器工具
	@Autowired
	private VariableScheduler variableScheduler; // 变量调度器
	@Autowired
	private UserFactory userFactory; // 用户工厂
	
	public ProcessContainerProxy getProcessContainer() {
		return processContainer;
	}
	public TaskHandlerUtil getTaskHandlerUtil() {
		return taskHandlerUtil;
	}
	public VariableScheduler getVariableScheduler() {
		return variableScheduler;
	}
	public UserFactory getUserFactory() {
		return userFactory;
	}
}
