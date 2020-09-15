package com.douglei.bpm.module.runtime;

import com.douglei.bpm.bean.annotation.ProcessEngineBean;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 
 * @author DougLei
 */
@ProcessEngineBean
public class RuntimeModule{

	@Transaction(beginTransaction=false)
	public void test() {
		System.out.println(SessionContext.getSession());
		System.out.println(1);
	}
}
