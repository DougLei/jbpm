package com.douglei.bpm.module.runtime.task;

import java.util.Date;
import java.util.UUID;

import com.douglei.bpm.bean.annotation.Property;
import com.douglei.bpm.process.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class Task {
	protected int id;
	@Property protected int procdefId;
	@Property protected String procinstId;
	@Property protected String taskinstId;
	@Property protected String parentTaskinstId;
	@Property protected Integer childrenNum;
	@Property protected String key;
	@Property protected String name;
	@Property protected String type;
	@Property protected Date startTime;
	@Property protected Date expiryTime;
	@Property protected String businessId;
	@Property protected String pageId;
	
	public Task() {}
	public Task(int procdefId, String procinstId, String parentTaskinstId, TaskMetadata taskMetadata) {
		this.procdefId = procdefId;
		this.procinstId = procinstId;
		this.taskinstId = UUID.randomUUID().toString();
		this.parentTaskinstId = parentTaskinstId;
		this.key = taskMetadata.getId();
		this.name = taskMetadata.getName();
		this.type = taskMetadata.getType().getName();
		this.startTime = new Date();
		this.pageId = taskMetadata.getPageID();
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
	public String getParentTaskinstId() {
		return parentTaskinstId;
	}
	public void setParentTaskinstId(String parentTaskinstId) {
		this.parentTaskinstId = parentTaskinstId;
	}
	public Integer getChildrenNum() {
		return childrenNum;
	}
	public void setChildrenNum(Integer childrenNum) {
		this.childrenNum = childrenNum;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
}
