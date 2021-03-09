package com.douglei.bpm.querysql.metadata;

import java.util.Map;

import com.douglei.orm.mapping.metadata.AbstractMetadata;

/**
 * 
 * @author DougLei
 */
public class QuerySqlMetadata extends AbstractMetadata {
	private ContentMetadata content;
	private boolean existsRequired; // 是否存在必须的参数标准, 即是否有required=true的参数标准
	private Map<String, ParameterStandardMetadata> parameterStandardMap;
	
	public QuerySqlMetadata(String name) {
		super.name = name;
	}
	
	public void setContent(ContentMetadata content) {
		this.content = content;
	}
	public void setParameterStandardMap(boolean existsRequired, Map<String, ParameterStandardMetadata> parameterStandardMap) {
		this.parameterStandardMap = parameterStandardMap;
	}

	public ContentMetadata getContent() {
		return content;
	}
	public boolean existsRequired() {
		return existsRequired;
	}
	public Map<String, ParameterStandardMetadata> getParameterStandardMap() {
		return parameterStandardMap;
	}
}
