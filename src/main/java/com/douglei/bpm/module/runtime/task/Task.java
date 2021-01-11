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
	@Property protected Integer joinBranchNum;
	@Property protected Integer forkBranchNum;
	@Property protected String key;
	@Property protected String name;
	@Property protected String type;
	@Property protected Date startTime;
	@Property protected Date expiryTime;
	@Property protected String businessId;
	@Property protected String pageId;
	@Property protected String userId;
	private Integer isAllClaimed;
	
	public Task() {}
	public Task(int procdefId, String procinstId, String parentTaskinstId, Date startTime, TaskMetadata taskMetadata) {
		this.procdefId = procdefId;
		this.procinstId = procinstId;
		this.taskinstId = UUID.randomUUID().toString();
		this.parentTaskinstId = parentTaskinstId;
		this.key = taskMetadata.getId();
		this.name = taskMetadata.getName();
		this.type = taskMetadata.getType().getName();
		this.startTime = startTime;
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
	public Integer getJoinBranchNum() {
		return joinBranchNum;
	}
	public void setJoinBranchNum(Integer joinBranchNum) {
		this.joinBranchNum = joinBranchNum;
	}
	public Integer getForkBranchNum() {
		return forkBranchNum;
	}
	public void setForkBranchNum(Integer forkBranchNum) {
		this.forkBranchNum = forkBranchNum;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 是否全部认领
	 * @return
	 */
	public boolean isAllClaimed() {
		return isAllClaimed != null && isAllClaimed == 1;
	}
	public Integer getIsAllClaimed() {
		return isAllClaimed;
	}
	public void setIsAllClaimed(Integer isAllClaimed) {
		this.isAllClaimed = isAllClaimed;
	}
}
