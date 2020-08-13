package com.douglei.bpm.module.repository;

import com.douglei.bpm.ProcessEngineBean;
import com.douglei.bpm.ProcessEngineField;
import com.douglei.bpm.module.repository.type.ProcessTypeService;

/**
 * 
 * @author DougLei
 */
@ProcessEngineBean
public class RepositoryModule {
	
	/**
	 * 流程类型服务类
	 */
	@ProcessEngineField
	private ProcessTypeService processTypeService;
	
	public ProcessTypeService getProcessTypeService() {
		return processTypeService;
	}
}
