package com.douglei.bpm.module.repository;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionService;
import com.douglei.bpm.module.repository.type.ProcessTypeService;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction = false)
public class RepositoryModule {
	
	@Attribute
	private ProcessTypeService typeService;
	
	@Attribute
	private ProcessDefinitionService definitionService;

	public ProcessTypeService getTypeService() {
		return typeService;
	}
	public ProcessDefinitionService getDefinitionService() {
		return definitionService;
	}
}
