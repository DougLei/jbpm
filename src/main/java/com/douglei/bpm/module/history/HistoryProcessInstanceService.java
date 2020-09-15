package com.douglei.bpm.module.history;

import com.douglei.bpm.bean.annotation.ProcessEngineBean;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 历史实例服务
 * @author DougLei
 */
@ProcessEngineBean
public class HistoryProcessInstanceService {
	
	@Transaction
	public boolean existsInstance(int processDefinitionId) {
		return true;
	}
}
