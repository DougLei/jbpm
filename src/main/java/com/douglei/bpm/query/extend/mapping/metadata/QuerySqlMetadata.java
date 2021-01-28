package com.douglei.bpm.query.extend.mapping.metadata;

import java.util.HashMap;
import java.util.Map;

import com.douglei.orm.mapping.metadata.AbstractMetadata;

/**
 * 
 * @author DougLei
 */
public class QuerySqlMetadata extends AbstractMetadata{
	private ContentMetadata content;
	private Map<String, ParameterStandardMetadata> parameterStandardMap;
	
	public QuerySqlMetadata(String name) {
		super.name = name;
	}
	
	/**
	 * 设置sql内容
	 * @param content
	 */
	public void setContent(ContentMetadata content) {
		this.content = content;
	}
	
	/**
	 * 添加参数标准
	 * @param metadata
	 */
	public void addParameterStandard(ParameterStandardMetadata metadata) {
		if(parameterStandardMap == null)
			parameterStandardMap = new HashMap<String, ParameterStandardMetadata>();
		else if(parameterStandardMap.containsKey(metadata.getName()))
			throw new IllegalArgumentException("重复配置了name为["+metadata.getName()+"]的<parameter-standard>元素");
		parameterStandardMap.put(metadata.getName(), metadata);
	}

	public ContentMetadata getContent() {
		return content;
	}
	public Map<String, ParameterStandardMetadata> getParameterStandardMap() {
		return parameterStandardMap;
	}
}
