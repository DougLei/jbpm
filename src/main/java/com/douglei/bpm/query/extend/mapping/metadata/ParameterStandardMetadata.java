package com.douglei.bpm.query.extend.mapping.metadata;

import java.io.Serializable;
import java.util.ArrayList;
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
	
	/**
	 * 添加参数支持的操作实体
	 * @param entity
	 */
	public void addOperatorEntity(OperatorEntity entity) {
		if(operatorEntities == null)
			operatorEntities = new ArrayList<OperatorEntity>();
		else if(operatorEntities.contains(entity))
			throw new IllegalArgumentException("重复配置了name为["+entity.getOperator()+"]的<operator>元素");
		operatorEntities.add(entity);
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
