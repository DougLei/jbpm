package com.douglei.bpm.module.history.instance;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;

/**
 * 历史实例服务
 * @author DougLei
 */
@Bean
public class InstanceService {
	
	/**
	 * 处理指定id的流程定义, 相关的所有历史实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(int processDefinitionId, InstanceHandlePolicy policy) {
		// TODO Auto-generated method stub
			
			
	}
	
	/**
	 * 判断指定id的流程定义, 是否存在历史实例
	 * @param processDefinitionId
	 * @return
	 */
	public boolean exists(int processDefinitionId) {
		// TODO 判断指定的流程定义id, 是否存在历史实例
		return false;
	}
}
