package com.douglei.bpm.module.repository;

import com.douglei.bpm.annotation.ProcessEngineBean;
import com.douglei.bpm.annotation.ProcessEngineField;
import com.douglei.bpm.module.repository.defined.ProcessDefinedService;
import com.douglei.bpm.module.repository.type.ProcessTypeService;

/**
 * 
 * @author DougLei
 */
@ProcessEngineBean
public class RepositoryModule {
	
	@ProcessEngineField
	private ProcessTypeService processTypeService;
	
	@ProcessEngineField
	private ProcessDefinedService processDefinedService;
	
	
	public ProcessTypeService getProcessTypeService() {
		return processTypeService;
	}
	public ProcessDefinedService getProcessDefinedService() {
		return processDefinedService;
	}
}
