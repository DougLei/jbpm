package com.douglei.bpm.module.runtime;

import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行流程实例服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class RuntimeProcessInstanceService {
	
	@Transaction
	public boolean existsInstance(int processDefinitionId) {
		return true;
	}

	/**
	 * 处理指定流程定义相关的所有实例
	 * @param processDefinitionId
	 * @param policy 对运行实例的处理策略
	 * @return
	 */
	public ExecutionResult processingAllInstance(int processDefinitionId, InstanceProcessingPolicy policy) {
		// TODO Auto-generated method stub
		return null;
	}
}
