package com.douglei.bpm.module.repository.defined;

import com.douglei.bpm.annotation.ProcessEngineTransactionBean;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程部署服务
 * @author DougLei
 */
@ProcessEngineTransactionBean
public class ProcessDefinedService {
	
	
	@Transaction
	public void deploy() {
		
	}
}
