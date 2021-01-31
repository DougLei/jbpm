package com.douglei.bpm.module.runtime.variable;

import java.math.BigDecimal;
import java.util.Date;

import com.douglei.bpm.process.handler.VariableEntity;
import com.douglei.tools.JdkSerializeUtil;
import com.douglei.tools.datatype.DateFormatUtil;

/**
 * 
 * @author DougLei
 */
public class Variable {
	protected int id;
	protected String procinstId;
	protected String taskinstId;
	protected Scope scope;
	protected String name;
	protected DataType dataType;
	protected String stringVal;
	protected BigDecimal numberVal;
	protected Date dateVal;
	protected byte[] objectVal;
	
	public Variable() {}
	public Variable(String procinstId, String taskinstId, VariableEntity variableEntity) {
		this.procinstId = procinstId;
		this.taskinstId = taskinstId;
		this.scope = variableEntity.getScope();
		this.name = variableEntity.getName();
		this.dataType = variableEntity.getDataType();
		if(variableEntity.getValue() != null) {
			switch (this.dataType) {
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
					this.objectVal = JdkSerializeUtil.serialize2ByteArray(new ObjectValue(variableEntity.getValue()));
					break;
			}
		}
	}
	
	/**
	 * 获取变量的值; 根据不同类型, 获取对应的值
	 * @return
	 */
	public Object getValue() {
		switch(dataType) {
			case STRING:
				return stringVal;
			case NUMBER:
				return numberVal;
			case DATETIME:
				return dateVal;
			case OBJECT:
				if(objectVal != null)
					return JdkSerializeUtil.deserializeFromByteArray(ObjectValue.class, objectVal).getValue();
		}
		return null;
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
	public Scope getScopeInstance() {
		return scope;
	}
	public void setScopeInstance(Scope scope) {
		this.scope = scope;
	}
	public String getScope() {
		return scope.name();
	}
	public void setScope(String scope) {
		this.scope = Scope.valueOf(scope);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DataType getDataTypeInstance() {
		return dataType;
	}
	public void setDataTypeInstance(DataType dataType) {
		this.dataType = dataType;
	}
	public String getDataType() {
		return dataType.name();
	}
	public void setDataType(String dataType) {
		this.dataType = DataType.valueOf(dataType);
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
