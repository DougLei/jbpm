package com.douglei.bpm.module.history;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.history.instance.HistoryProcessInstanceService;
import com.douglei.bpm.module.history.task.HistoryTaskService;

/**
 * 
 * @author DougLei
 */
@Bean
public class HistoryModule {
	
	@Autowired
	private HistoryProcessInstanceService processInstanceService;
	
	@Autowired
	private HistoryTaskService taskService;

	public HistoryProcessInstanceService getHistoryProcessInstanceService() {
		return processInstanceService;
	}
	public HistoryTaskService getHistoryTaskService() {
		return taskService;
	}
}
