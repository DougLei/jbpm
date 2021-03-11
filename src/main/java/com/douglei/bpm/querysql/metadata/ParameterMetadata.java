package com.douglei.bpm.querysql.metadata;

import com.douglei.orm.mapping.metadata.Metadata;

/**
 * 
 * @author DougLei
 */
public class ParameterMetadata implements Metadata {
	private String name;
	private DataType dataType;
	private boolean required;
	
	public ParameterMetadata(String name, DataType dataType, boolean required) {
		this.name = name;
		this.dataType = dataType;
		this.required = required;
	}

	public String getName() {
		return name;
	}
	public DataType getDataType() {
		return dataType;
	}
	public boolean isRequired() {
		return required;
	}
}
