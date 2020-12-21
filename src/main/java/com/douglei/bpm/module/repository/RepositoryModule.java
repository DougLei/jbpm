package com.douglei.bpm.module.repository;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionService;
import com.douglei.bpm.module.repository.delegation.ProcessDelegationService;
import com.douglei.bpm.module.repository.suggest.SuggestService;
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

	@Autowired
	private ProcessDelegationService delegationService;
	
	@Autowired
	private SuggestService suggestService;
	
	
	public ProcessTypeService getTypeService() {
		return typeService;
	}
	public ProcessDefinitionService getDefinitionService() {
		return definitionService;
	}
	public ProcessDelegationService getDelegationService() {
		return delegationService;
	}
	public SuggestService getSuggestService() {
		return suggestService;
	}
}
