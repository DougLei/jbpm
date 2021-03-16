package com.douglei.bpm.query;

import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryEntity;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.QueryMode;

/**
 * 
 * @author DougLei
 */
public interface SqlQueryExecutor {

	/**
	 * 获取查询器的查询模式
	 * @return
	 */
	QueryMode getMode();
	
	/**
	 * 执行查询
	 * @param entity
	 * @return
	 */
	Object execute(SQLQueryEntity entity);
	
	/**
	 * 执行查询
	 * @param clazz
	 * @param entity
	 * @return
	 */
	<T> Object execute(Class<T> clazz, SQLQueryEntity entity);
}
