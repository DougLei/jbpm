package com.douglei.bpm.query.impl;

import com.douglei.bpm.query.SqlQueryExecutor;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryEntity;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.QueryMode;

/**
 * 
 * @author DougLei
 */
public class CountQueryExecutor implements SqlQueryExecutor {
	
	@Override
	public QueryMode getMode() {
		return QueryMode.COUNT_QUERY;
	}

	@Override
	public Long execute(SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().countQuery(entity);
	}

	@Override
	public <T> Long execute(Class<T> clazz, SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().countQuery(entity);
	}
}
