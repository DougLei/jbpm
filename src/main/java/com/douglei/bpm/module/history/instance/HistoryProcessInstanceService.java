package com.douglei.bpm.module.history.instance;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceHandlePolicy;

/**
 * 历史实例服务
 * @author DougLei
 */
@Bean
public class HistoryProcessInstanceService {
	
	/**
	 * 判断指定的流程定义, 是否存在历史实例
	 * @param processDefinition
	 * @return
	 */
	public boolean exists(ProcessDefinition processDefinition) {
		// TODO 
		return false;
	}
	
	/**
	 * 处理指定的流程定义, 相关的所有历史实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(ProcessDefinition processDefinition, ProcessInstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
	}
}
