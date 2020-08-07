package com.douglei.bpm.module;

import com.douglei.bpm.ProcessEngineBean;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.context.transaction.component.TransactionComponent;

/**
 * 
 * @author DougLei
 */
@ProcessEngineBean
@TransactionComponent
public class RuntimeModule{

	@Transaction(beginTransaction=false)
	public void test() {
		System.out.println(SessionContext.getSession());
		System.out.println(1);
	}
}
