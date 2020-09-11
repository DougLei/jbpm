package com.douglei.bpm.module.history;

import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 历史实例服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class HistoryProcessInstanceService {
	
	@Transaction
	public boolean existsInstance(int processDefinitionId) {
		return true;
	}
}
