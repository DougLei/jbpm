package com.douglei.bpm.module.repository;

import com.douglei.bpm.annotation.ProcessEngineBean;
import com.douglei.bpm.annotation.ProcessEngineField;
import com.douglei.bpm.module.repository.deploy.ProcessDeployService;
import com.douglei.bpm.module.repository.type.ProcessTypeService;

/**
 * 
 * @author DougLei
 */
@ProcessEngineBean
public class RepositoryModule {
	
	@ProcessEngineField
	private ProcessTypeService typeService;
	
	@ProcessEngineField
	private ProcessDeployService deployService;

	
	public ProcessTypeService getTypeService() {
		return typeService;
	}
	public ProcessDeployService getDeployService() {
		return deployService;
	}
}
