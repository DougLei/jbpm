package com.douglei.bpm.query;

import java.util.List;

/**
 * 
 * @author DougLei
 */
public class QuerySqlEntity {
	private String sql; // 可执行的sql语句
	private List<Object> parameterValues; // 执行sql语句需要的参数集合

	QuerySqlEntity(String sql, List<Object> parameterValues) {
		this.sql = sql;
		this.parameterValues = parameterValues;
	}
	
	public String getSql() {
		return sql;
	}
	public List<Object> getParameterValues() {
		return parameterValues;
	}
}
