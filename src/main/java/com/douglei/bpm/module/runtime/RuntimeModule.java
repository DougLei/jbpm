package com.douglei.bpm.module.runtime;

import com.douglei.bpm.bean.Attribute;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.runtime.instance.RuntimeInstanceService;
import com.douglei.bpm.module.runtime.task.RuntimeTaskService;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction = false)
public class RuntimeModule{
	
	@Attribute
	private RuntimeInstanceService runtimeInstanceService;
	
	@Attribute
	private RuntimeTaskService runtimeTaskService;

	public RuntimeInstanceService getRuntimeInstanceService() {
		return runtimeInstanceService;
	}
	public RuntimeTaskService getRuntimeTaskService() {
		return runtimeTaskService;
	}
}
