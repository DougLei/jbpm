package com.douglei.bpm.module.history;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.history.instance.InstanceService;
import com.douglei.bpm.module.history.task.TaskService;

/**
 * 
 * @author DougLei
 */
@Bean
public class HistoryModule {
	
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
