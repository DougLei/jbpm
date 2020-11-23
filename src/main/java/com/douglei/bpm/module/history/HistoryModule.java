package com.douglei.bpm.module.history;

import com.douglei.bpm.bean.Autowired;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.history.instance.HistoryInstanceService;
import com.douglei.bpm.module.history.task.HistoryTaskService;

/**
 * 
 * @author DougLei
 */
@Bean
public class HistoryModule {
	
	@Autowired
	private HistoryInstanceService historyInstanceService;
	
	@Autowired
	private HistoryTaskService historyTaskService;

	public HistoryInstanceService getHistoryInstanceService() {
		return historyInstanceService;
	}
	public HistoryTaskService getHistoryTaskService() {
		return historyTaskService;
	}
}
