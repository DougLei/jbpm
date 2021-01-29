package com.douglei.bpm.query;

import java.util.List;

/**
 * 
 * @author DougLei
 */
public class QuerySqlEntity {
	private String sql; // 可执行的sql语句
	private List<Object> parameters; // 执行sql语句需要的参数集合

	QuerySqlEntity(String sql, List<Object> parameters) {
		this.sql = sql;
		this.parameters = parameters;
	}
	
	public String getSql() {
		return sql;
	}
	public List<Object> getParameters() {
		return parameters;
	}
}
