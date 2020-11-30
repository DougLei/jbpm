package com.douglei.bpm.module.runtime.instance.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author DougLei
 */
public class ProcessVariable {
	protected int id;
	protected int procdefId;
	protected int procinstId;
	protected int taskId;
	protected int scope;
	protected String name;
	protected String dataType;
	protected String stringVal;
	protected BigDecimal numberVal;
	protected Date dateVal;
	
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
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
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
}
