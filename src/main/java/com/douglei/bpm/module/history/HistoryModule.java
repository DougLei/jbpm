package com.douglei.bpm.module.history;

import com.douglei.bpm.bean.Autowire;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.history.instance.HistoryInstanceService;
import com.douglei.bpm.module.history.task.HistoryTaskService;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction = false)
public class HistoryModule {
	
	@Autowire
	private HistoryInstanceService historyInstanceService;
	
	@Autowire
	private HistoryTaskService historyTaskService;

	public HistoryInstanceService getHistoryInstanceService() {
		return historyInstanceService;
	}
	public HistoryTaskService getHistoryTaskService() {
		return historyTaskService;
	}
}
