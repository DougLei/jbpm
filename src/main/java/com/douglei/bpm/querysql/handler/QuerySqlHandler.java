package com.douglei.bpm.querysql.handler;

import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.sql.query.page.PageResult;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction=true)
public class QuerySqlHandler {
	
	@Autowired
	private SessionFactoryContainer container;
	
	/**
	 * 
	 * @param clazz
	 * @param name
	 * @param parameters
	 * @return
	 */
	@Transaction
	public <T> List<T> query(Class<T> clazz, String name, List<AbstractParameter> parameters){
		QuerySqlEntity entity = new QuerySqlAssembler(name, parameters, container).getQuerySqlEntity();
		return SessionContext.getSqlSession().query(clazz,  entity.getSql(), entity.getParameterValues());
	}
	
	/**
	 * 
	 * @param clazz
	 * @param name
	 * @param parameters
	 * @return
	 */
	@Transaction
	public <T> T uniqueQuery(Class<T> clazz, String name, List<AbstractParameter> parameters) {
		// TODO
		return null;
	}
	
	/**
	 * 
	 * @param clazz
	 * @param name
	 * @param parameters
	 * @return
	 */
	@Transaction
	public <T> PageResult<T> pageQuery(Class<T> clazz, String name, List<AbstractParameter> parameters) {
		// TODO
		return null;
	}
}
