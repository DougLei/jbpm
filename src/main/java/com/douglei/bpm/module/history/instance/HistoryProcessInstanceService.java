package com.douglei.bpm.module.history.instance;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceHandlePolicy;

/**
 * 历史实例服务
 * @author DougLei
 */
@Bean
public class HistoryProcessInstanceService {
	
	/**
	 * 判断指定code和version的流程, 是否存在历史实例
	 * @param processDefinitionId
	 * @return
	 */
	public boolean exists(int processDefinitionId) {
		// TODO 判断指定的流程定义id, 是否存在历史实例
		return false;
	}
	
	/**
	 * 处理指定id的流程, 相关的所有历史实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(int processDefinitionId, ProcessInstanceHandlePolicy policy) {
		// TODO Auto-generated method stub
	}

	/**
	 * 判断指定code和version的流程, 是否存在历史实例
	 * @param code
	 * @param version
	 * @param tenantId
	 * @return
	 */
	public boolean exists(String code, String version, String tenantId) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 处理指定code和version的流程, 相关的所有历史实例
	 * @param code
	 * @param version
	 * @param tenantId
	 * @param policy 对实例的处理策略
	 */
	public void handle(String code, String version, String tenantId, ProcessInstanceHandlePolicy policy) {
		// TODO Auto-generated method stub
		
	}
}
