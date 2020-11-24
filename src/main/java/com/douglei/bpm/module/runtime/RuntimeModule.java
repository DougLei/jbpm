package com.douglei.bpm.module.runtime;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.instance.InstanceService;
import com.douglei.bpm.module.runtime.task.TaskService;

/**
 * 
 * @author DougLei
 */
@Bean
public class RuntimeModule{
	
	@Autowired
	private InstanceService instanceService;
	
	@Autowired
	private TaskService taskService;

	public InstanceService getInstanceService() {
		return instanceService;
	}
	public TaskService getTaskService() {
		return taskService;
	}
}
