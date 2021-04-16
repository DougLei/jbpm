package com.douglei.bpm.module.repository;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinitionService;
import com.douglei.bpm.module.repository.delegation.DelegationService;
import com.douglei.bpm.module.repository.suggest.SuggestService;
import com.douglei.bpm.module.repository.type.TypeService;

/**
 * 
 * @author DougLei
 */
@Bean
public class RepositoryModule {
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private SuggestService suggestService;
	
	@Autowired
	private DelegationService delegationService;
	
	@Autowired
	private ProcessDefinitionService definitionService;

	/**
	 * 获取类型服务实例
	 * @return
	 */
	public TypeService getTypeService() {
		return typeService;
	}
	/**
	 * 获取常用意见服务实例
	 * @return
	 */
	public SuggestService getSuggestService() {
		return suggestService;
	}
	/**
	 * 获取委托服务实例
	 * @return
	 */
	public DelegationService getDelegationService() {
		return delegationService;
	}
	/**
	 * 获取流程定义服务实例
	 * @return
	 */
	public ProcessDefinitionService getDefinitionService() {
		return definitionService;
	}
}
