package com.douglei.bpm.querysql.metadata;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class ParameterStandardMetadata implements Serializable {
	private String name;
	private DataType dataType;
	private boolean required;
	private List<OperatorEntity> operatorEntities;
	
	public ParameterStandardMetadata(String name, DataType dataType, boolean required) {
		this.name = name;
		this.dataType = dataType;
		this.required = required;
	}
	public void setOperatorEntities(List<OperatorEntity> operatorEntities) {
		this.operatorEntities = operatorEntities;
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
	public List<OperatorEntity> getOperatorEntities() {
		return operatorEntities;
	}
}
