package com.douglei.bpm.module.runtime.variable;

import java.math.BigDecimal;
import java.util.Date;

import com.douglei.bpm.process.handler.components.variable.VariableEntity;
import com.douglei.tools.utils.datatype.dateformat.DateFormatUtil;
import com.douglei.tools.utils.serialize.JdkSerializeProcessor;

/**
 * 
 * @author DougLei
 */
public class Variable {
	protected int id;
	protected String procinstId;
	protected String taskinstId;
	protected String scope;
	protected String name;
	protected String dataType;
	protected String stringVal;
	protected BigDecimal numberVal;
	protected Date dateVal;
	protected byte[] objectVal;
	
	public Variable() {}
	public Variable(String procinstId, String taskinstId, VariableEntity variableEntity) {
		this.procinstId = procinstId;
		this.taskinstId = taskinstId;
		this.scope = variableEntity.getScope().name();
		this.name = variableEntity.getName();
		this.dataType = variableEntity.getDataType().name();
		if(variableEntity.getValue() != null) {
			switch (variableEntity.getDataType()) {
				case STRING:
					this.stringVal = variableEntity.getValue().toString();
					break;
				case NUMBER:
					this.numberVal = new BigDecimal(variableEntity.getValue().toString());
					break;
				case DATETIME:
					this.dateVal = DateFormatUtil.parseDate(variableEntity.getValue());
					break;
				case OBJECT:
					this.objectVal = JdkSerializeProcessor.serialize2ByteArray(new ObjectValue(variableEntity.getValue()));
					break;
			}
		}
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProcinstId() {
		return procinstId;
	}
	public void setProcinstId(String procinstId) {
		this.procinstId = procinstId;
	}
	public String getTaskinstId() {
		return taskinstId;
	}
	public void setTaskinstId(String taskinstId) {
		this.taskinstId = taskinstId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getStringVal() {
		return stringVal;
	}
	public void setStringVal(String stringVal) {
		this.stringVal = stringVal;
	}
	public BigDecimal getNumberVal() {
		return numberVal;
	}
	public void setNumberVal(BigDecimal numberVal) {
		this.numberVal = numberVal;
	}
	public Date getDateVal() {
		return dateVal;
	}
	public void setDateVal(Date dateVal) {
		this.dateVal = dateVal;
	}
	public byte[] getObjectVal() {
		return objectVal;
	}
	public void setObjectVal(byte[] objectVal) {
		this.objectVal = objectVal;
	}
}
