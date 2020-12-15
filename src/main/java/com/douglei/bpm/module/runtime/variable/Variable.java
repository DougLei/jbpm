package com.douglei.bpm.module.runtime.variable;

import java.math.BigDecimal;
import java.util.Date;

import com.douglei.tools.utils.datatype.dateformat.DateFormatUtil;
import com.douglei.tools.utils.serialize.JdkSerializeProcessor;

/**
 * 
 * @author DougLei
 */
public class Variable {
	protected int id;
	protected int procdefId;
	protected int procinstId;
	protected Integer taskId;
	protected String scope;
	protected String name;
	protected String dataType;
	protected String stringVal;
	protected BigDecimal numberVal;
	protected Date dateVal;
	protected byte[] objectVal;
	
	public Variable() {}
	public Variable(int procdefId, int procinstId, Integer taskId, VariableEntity processVariable) {
		this.procdefId = procdefId;
		this.procinstId = procinstId;
		this.taskId = taskId;
		this.scope = processVariable.getScope().getName();
		this.name = processVariable.getName();
		this.dataType = processVariable.getDataType().getName();
		if(processVariable.getValue() != null) {
			switch (processVariable.getDataType()) {
				case STRING:
					this.stringVal = processVariable.getValue().toString();
					break;
				case NUMBER:
					this.numberVal = new BigDecimal(processVariable.getValue().toString());
					break;
				case DATETIME:
					this.dateVal = DateFormatUtil.parseDate(processVariable.getValue());
					break;
				case OBJECT:
					this.objectVal = JdkSerializeProcessor.serialize2ByteArray(new ObjectValue(processVariable.getValue()));
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
	public int getProcdefId() {
		return procdefId;
	}
	public void setProcdefId(int procdefId) {
		this.procdefId = procdefId;
	}
	public int getProcinstId() {
		return procinstId;
	}
	public void setProcinstId(int procinstId) {
		this.procinstId = procinstId;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
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
