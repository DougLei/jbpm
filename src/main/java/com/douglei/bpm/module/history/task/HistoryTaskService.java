package com.douglei.bpm.module.history.task;

import com.douglei.bpm.bean.Autowire;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.process.ProcessHandler;

/**
 * 历史任务服务
 * @author DougLei
 */
@Bean
public class HistoryTaskService {
	
	@Autowire
	private ProcessHandler processHandler;
	
	
	
}
