package com.douglei.bpm.module.history.instance;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.instance.HandlePolicy;

/**
 * 历史实例服务
 * @author DougLei
 */
@Bean
public class HistoryProcessInstanceService {
	
	/**
	 * 处理指定的流程定义, 相关的所有历史实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(int processDefinitionId, HandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
	}
}
