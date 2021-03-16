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
public class LimitQueryExecutor implements QueryExecutor {
	private int startRow;
	private int length;
	
	/**
	 * 
	 * @param startRow 起始的行数, 值从1开始
	 * @param length 查询的数据长度
	 */
	public LimitQueryExecutor(int startRow, int length) {
		this.startRow = startRow;
		this.length = length;
	}

	@Override
	public QueryMode getMode() {
		return QueryMode.LIMIT_QUERY;
	}

	@Override
	public List<Map<String, Object>> execute(SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().limitQuery(startRow, length, entity);
	}

	@Override
	public <T> List<T> execute(Class<T> clazz, SQLQueryEntity entity) {
		return SessionContext.getSQLQuerySession().limitQuery(clazz, startRow, length, entity);
	}
}
