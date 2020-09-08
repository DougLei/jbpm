package com.douglei.bpm.module.runtime;

import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
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
}
