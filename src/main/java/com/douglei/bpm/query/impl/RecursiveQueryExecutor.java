package com.douglei.bpm.query.impl;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.query.QueryExecutor;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryEntity;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.QueryMode;
import com.douglei.orm.sessionfactory.sessions.sqlsession.RecursiveEntity;

/**
 * 
 * @author DougLei
 */
public class RecursiveQueryExecutor implements QueryExecutor {
	private RecursiveEntity entity1;
	
	/**
	 * 
	 * @param entity1
	 */
	public RecursiveQueryExecutor(RecursiveEntity entity1) {
		this.entity1 = entity1;
	}

	@Override
	public QueryMode getMode() {
		return QueryMode.RECURSIVE_QUERY;
	}

	@Override
	public List<Map<String, Object>> execute(SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().recursiveQuery(entity1, entity);
	}

	@Override
	public <T> List<T> execute(Class<T> clazz, SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().recursiveQuery(clazz, entity1, entity);
	}
}
