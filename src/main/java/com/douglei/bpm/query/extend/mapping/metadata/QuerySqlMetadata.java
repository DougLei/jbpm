package com.douglei.bpm.query.extend.mapping.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class QuerySqlMetadata implements Serializable{
	private String name;
	private String content;
	private List<ParameterStandardMetadata> parameterStandards;
	
	public QuerySqlMetadata(String name, String content) {
		this.name = name;
		this.content = content;
	}
	
	/**
	 * 添加参数标准
	 * @param metadata
	 */
	public void addParameterStandard(ParameterStandardMetadata metadata) {
		if(parameterStandards == null)
			parameterStandards = new ArrayList<ParameterStandardMetadata>();
		else if(parameterStandards.contains(metadata))
			throw new IllegalArgumentException("在["+name+"]映射中, 重复配置了name为["+metadata.getName()+"]的<parameter-standard>元素");
		parameterStandards.add(metadata);
	}
}
