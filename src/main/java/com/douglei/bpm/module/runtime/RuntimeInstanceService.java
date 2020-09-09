package com.douglei.bpm.module.runtime;

import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.bpm.module.common.service.ExecutionResult;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class RuntimeInstanceService {
	
	@Transaction
	public boolean existsInstance(int processDefinedId) {
		return true;
	}

	/**
	 * 激活指定流程定义相关的所有流程实例
	 * @param processDefinedId
	 * @return
	 */
	public ExecutionResult activateAllProcessInstance(int processDefinedId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 挂起指定流程定义相关的所有流程实例
	 * @param processDefinedId
	 * @return
	 */
	@Transaction
	public ExecutionResult suspendAllProcessInstance(int processDefinedId) {
		// TODO Auto-generated method stub
		return null;
	}
}