package com.douglei.bpm.query.extend.mapping.metadata;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
public class ParameterStandardMetadata implements Serializable {
	private String prefix;
	private String name;
	private DataType dataType;
	private boolean required;
	private Operator[] operator;
	
	public String getPrefix() {
		return prefix;
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
	public Operator[] getOperator() {
		return operator;
	}
}
