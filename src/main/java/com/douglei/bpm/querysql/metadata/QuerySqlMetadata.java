package com.douglei.bpm.querysql.metadata;

import java.util.Map;

import com.douglei.orm.mapping.metadata.AbstractMetadata;

/**
 * 
 * @author DougLei
 */
public class QuerySqlMetadata extends AbstractMetadata {
	private String sql;
	private Map<String, ParameterMetadata> parameterMap;
	
	public QuerySqlMetadata(String name, String sql) {
		this.name = name;
		this.sql = sql;
	}
	public void setParameterMap(Map<String, ParameterMetadata> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public String getSql() {
		return sql;
	}
	public Map<String, ParameterMetadata> getParameterMap() {
		return parameterMap;
	}
}
