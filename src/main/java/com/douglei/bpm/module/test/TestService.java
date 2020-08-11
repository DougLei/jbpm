package com.douglei.bpm.module.test;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.context.transaction.component.TransactionComponent;

@TransactionComponent
public class TestService {

	@Transaction
	public long countQuery() {
		return SessionContext.getSqlSession().countQuery("select * from bpm_re_listener");
	}
}
