package com.douglei.bpm.query.extend.mapping.metadata;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
public class ParameterStandardMetadata implements Serializable {
	private String name;
	private DataType dataType;
	private boolean required;
	private OperatorEntity[] operatorEntities;
	
	public String getName() {
		return name;
	}
	public DataType getDataType() {
		return dataType;
	}
	public boolean isRequired() {
		return required;
	}
	public OperatorEntity[] getOperatorEntities() {
		return operatorEntities;
	}
}
