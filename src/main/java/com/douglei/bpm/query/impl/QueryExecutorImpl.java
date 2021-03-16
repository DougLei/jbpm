package com.douglei.bpm.query.impl;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.query.QueryExecutor;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryEntity;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.QueryMode;

/**
 * 
 * @author DougLei
 */
public class QueryExecutorImpl implements QueryExecutor {

	@Override
	public QueryMode getMode() {
		return QueryMode.QUERY;
	}

	@Override
	public List<Map<String, Object>> execute(SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().query(entity);
	}

	@Override
	public <T> List<T> execute(Class<T> clazz, SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().query(clazz, entity);
	}
}
