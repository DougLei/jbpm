package com.douglei.bpm.module.repository;

import com.douglei.bpm.bean.Autowired;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionService;
import com.douglei.bpm.module.repository.type.ProcessTypeService;

/**
 * 
 * @author DougLei
 */
@Bean
public class RepositoryModule {
	
	@Autowired
	private ProcessTypeService typeService;
	
	@Autowired
	private ProcessDefinitionService definitionService;

	public ProcessTypeService getTypeService() {
		return typeService;
	}
	public ProcessDefinitionService getDefinitionService() {
		return definitionService;
	}
}
