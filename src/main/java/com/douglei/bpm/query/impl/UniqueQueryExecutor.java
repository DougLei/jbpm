package com.douglei.bpm.query.impl;

import java.util.Map;

import com.douglei.bpm.query.QueryExecutor;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryEntity;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.QueryMode;

/**
 * 
 * @author DougLei
 */
public class UniqueQueryExecutor implements QueryExecutor {

	@Override
	public QueryMode getMode() {
		return QueryMode.UNIQUE_QUERY;
	}

	@Override
	public Map<String, Object> execute(SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().uniqueQuery(entity);
	}

	@Override
	public <T> T execute(Class<T> clazz, SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().uniqueQuery(clazz, entity);
	}
}
