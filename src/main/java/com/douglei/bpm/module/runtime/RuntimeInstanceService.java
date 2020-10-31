package com.douglei.bpm.module.runtime;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.instance.ProcessInstanceHandlePolicy;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行流程实例服务
 * @author DougLei
 */
@Bean
public class RuntimeInstanceService {
	
	/**
	 * 判断指定id的流程定义, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public boolean exists(int processDefinitionId) {
		// TODO 判断指定的流程定义id, 是否存在运行实例
		return true;
	}

	/**
	 * 处理指定id的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void processInstances(int processDefinitionId, ProcessInstanceHandlePolicy policy) {
		// TODO Auto-generated method stub
			
			
	}
}
