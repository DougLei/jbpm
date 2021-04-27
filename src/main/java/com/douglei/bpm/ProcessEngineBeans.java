package com.douglei.bpm;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.APIContainer;
import com.douglei.bpm.process.handler.TaskHandleUtil;
import com.douglei.bpm.process.mapping.ProcessMappingContainer;

/**
 * 流程(可对外提供)的bean实例
 * @author DougLei
 */
@Bean
public class ProcessEngineBeans {
	
	@Autowired
	private ProcessMappingContainer processContainer; // 流程容器
	
	@Autowired
	private TaskHandleUtil taskHandleUtil; // 任务办理工具
	
	@Autowired
	private APIContainer apiContainer; // api容器
	
	// -------------------------------------------------------------------------------------------------
	public ProcessMappingContainer getProcessContainer() {
		return processContainer;
	}
	public TaskHandleUtil getTaskHandleUtil() {
		return taskHandleUtil;
	}
	public APIContainer getAPIContainer() {
		return apiContainer;
	}
}
