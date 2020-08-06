package com.douglei.bpm.component.re;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;
import com.douglei.orm.context.transaction.component.TransactionComponent;

@TransactionComponent
public class ProcessTypeService {

	@Transaction
	public void add() {
		long count = SessionContext.getSqlSession().countQuery("select count(1) from BPM_RE_PROCTYPE");
		System.out.println("BPM_RE_PROCTYPE 表中的数据数量为:"+count+" 条");
	}
}
