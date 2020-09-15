package com.douglei.bpm.module.repository;

import com.douglei.bpm.bean.annotation.ProcessEngineBean;
import com.douglei.bpm.bean.annotation.ProcessEngineField;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionService;
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
	private ProcessDefinitionService definitionService;

	
	public ProcessTypeService getTypeService() {
		return typeService;
	}
	public ProcessDefinitionService getDefinitionService() {
		return definitionService;
	}
}
