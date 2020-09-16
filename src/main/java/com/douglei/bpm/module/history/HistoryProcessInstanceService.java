package com.douglei.bpm.module.history;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 历史实例服务
 * @author DougLei
 */
@Bean
public class HistoryProcessInstanceService {
	
	@Transaction
	public boolean existsInstance(int processDefinitionId) {
		return true;
	}
}
