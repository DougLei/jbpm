package com.douglei.bpm.module.history.task;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.container.ProcessContainerProxy;

/**
 * 历史任务服务
 * @author DougLei
 */
@Bean
public class TaskService {
	
	@Autowired
	private ProcessContainerProxy processHandler;
	
	
	
}
