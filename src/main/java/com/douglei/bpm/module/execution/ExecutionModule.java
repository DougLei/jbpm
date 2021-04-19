package com.douglei.bpm.module.execution;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.execution.instance.ProcessInstanceService;
import com.douglei.bpm.module.execution.task.TaskService;

/**
 * 
 * @author DougLei
 */
@Bean
public class ExecutionModule {
	
	@Autowired
	private ProcessInstanceService processInstanceService;
	
	@Autowired
	private TaskService taskService;

	/**
	 * 获取流程实例服务实例
	 * @return
	 */
	public ProcessInstanceService getProcessInstanceService() {
		return processInstanceService;
	}
	/**
	 * 获取任务服务实例
	 * @return
	 */
	public TaskService getTaskService() {
		return taskService;
	}
}
