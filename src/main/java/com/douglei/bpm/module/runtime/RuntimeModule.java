package com.douglei.bpm.module.runtime;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.instance.RuntimeInstanceService;
import com.douglei.bpm.module.runtime.task.RuntimeTaskService;

/**
 * 
 * @author DougLei
 */
@Bean
public class RuntimeModule{
	
	@Autowired
	private RuntimeInstanceService runtimeInstanceService;
	
	@Autowired
	private RuntimeTaskService runtimeTaskService;

	public RuntimeInstanceService getRuntimeInstanceService() {
		return runtimeInstanceService;
	}
	public RuntimeTaskService getRuntimeTaskService() {
		return runtimeTaskService;
	}
}
