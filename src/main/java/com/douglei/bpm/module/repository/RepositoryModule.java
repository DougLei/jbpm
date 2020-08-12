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
	
	@ProcessEngineField
	private ProcessTypeService processTypeService;
}
