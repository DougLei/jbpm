package com.douglei.bpm.module.repository.type;

import com.douglei.bpm.ProcessEngineTransactionBean;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程类型服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class ProcessTypeService {
	
	/**
	 * 保存类型
	 * @param type
	 */
	@Transaction
	public void save(ProcessTypeEntity type) {
		
		
		
		SessionContext.getTableSession().save(type);
	}
}
