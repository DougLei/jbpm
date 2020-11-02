package com.douglei.bpm.module.history.task;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.ProcessHandler;

/**
 * 历史任务服务
 * @author DougLei
 */
@Bean
public class HistoryTaskService {
	
	@Attribute
	private ProcessHandler processHandler;
	
	
	
}
