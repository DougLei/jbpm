package com.douglei.bpm.query.impl;

import java.util.Map;

import com.douglei.bpm.query.SqlQueryExecutor;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryEntity;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.QueryMode;
import com.douglei.orm.sessionfactory.sessions.sqlsession.PageRecursiveEntity;
import com.douglei.orm.sql.query.page.PageResult;

/**
 * 
 * @author DougLei
 */
public class PageRecursiveQueryExecutor implements SqlQueryExecutor {
	private PageRecursiveEntity entity1;
	
	/**
	 * 
	 * @param entity1
	 */
	public PageRecursiveQueryExecutor(PageRecursiveEntity entity1) {
		this.entity1 = entity1;
	}

	@Override
	public QueryMode getMode() {
		return QueryMode.PAGE_RECURSIVE_QUERY;
	}

	@Override
	public PageResult<Map<String, Object>> execute(SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().pageRecursiveQuery(entity1, entity);
	}

	@Override
	public <T> PageResult<T> execute(Class<T> clazz, SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().pageRecursiveQuery(clazz, entity1, entity);
	}
}
