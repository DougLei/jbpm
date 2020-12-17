package com.douglei.bpm.module.runtime.task;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public class Task {
	protected int id;
	protected int procdefId;
	protected int procinstId;
	protected String key;
	protected String name;
	protected String type;
	protected Date startTime;
	protected Date expiryTime;
	protected String businessId;
	protected String pageId;
	
	public Task() {}
	public Task(int procdefId, int procinstId, TaskMetadata taskMetadata) {
		this.procdefId = procdefId;
		this.procinstId = procinstId;
		this.key = taskMetadata.getId();
		this.name = taskMetadata.getName();
		this.type = taskMetadata.getType().getName();
		this.startTime = new Date();
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
	
	private List<Object> taskIdParam;
	public List<Object> getTaskIdParam(){
		if(taskIdParam == null)
			taskIdParam = Arrays.asList(id);
		return taskIdParam;
	}
}