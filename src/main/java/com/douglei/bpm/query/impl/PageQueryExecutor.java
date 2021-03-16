package com.douglei.bpm.query.impl;

import java.util.Map;

import com.douglei.bpm.query.QueryExecutor;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryEntity;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.QueryMode;
import com.douglei.orm.sql.query.page.PageResult;

/**
 * 
 * @author DougLei
 */
public class PageQueryExecutor implements QueryExecutor {
	private int pageNum;
	private int pageSize;
	
	/**
	 * 
	 * @param pageNum 
	 * @param length 
	 */
	public PageQueryExecutor(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	@Override
	public QueryMode getMode() {
		return QueryMode.PAGE_QUERY;
	}

	@Override
	public PageResult<Map<String, Object>> execute(SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().pageQuery(pageNum, pageSize, entity);
	}

	@Override
	public <T> PageResult<T> execute(Class<T> clazz, SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().pageQuery(clazz, pageNum, pageSize, entity);
	}
}
