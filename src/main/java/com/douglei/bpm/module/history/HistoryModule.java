package com.douglei.bpm.module.history;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.history.instance.HistoryInstanceService;
import com.douglei.bpm.module.history.task.HistoryTaskService;

/**
 * 
 * @author DougLei
 */
@Bean
public class HistoryModule {
	
	@Autowired
	private HistoryInstanceService instanceService;
	
	@Autowired
	private HistoryTaskService taskService;

	public HistoryInstanceService getHistoryInstanceService() {
		return instanceService;
	}
	public HistoryTaskService getHistoryTaskService() {
		return taskService;
	}
}
