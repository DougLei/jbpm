package com.douglei.bpm.module.execution.task.runtime;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import com.douglei.bpm.bean.annotation.Property;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.orm.context.SessionContext;

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
	@Property private Integer assignCount;
	@Property protected String sourceKey;
	@Property protected String key;
	@Property protected String name;
	@Property protected String type;
	@Property protected Date startTime;
	@Property protected Date expiryTime;
	@Property protected Date suspendTime;
	@Property protected String businessId;
	@Property protected String pageId;
	@Property protected String userId;
	@Property protected String reason;
	@Property private Integer isAllClaimed;
	
	public Task() {}
	public Task(int procdefId, String procinstId, String parentTaskinstId, String sourceKey, TaskMetadata taskMetadata) {
		this.procdefId = procdefId;
		this.procinstId = procinstId;
		this.taskinstId = UUID.randomUUID().toString();
		this.parentTaskinstId = parentTaskinstId;
		this.sourceKey = sourceKey;
		this.key = taskMetadata.getId();
		this.name = taskMetadata.getName();
		this.type = taskMetadata.getType().getName();
		this.startTime = new Date();
		this.pageId = taskMetadata.getPageID();
	}
	
	/**
	 * 记录业务id
	 * @param businessId
	 */
	public void addBusinessId(String businessId) {
		if(businessId != null && this.businessId == null)
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set business_id=? where id=?", Arrays.asList(businessId, id));
	}
	
	/**
	 * 设置调度权限
	 * @param userId
	 */
	public void setDispatchRight(String userId) {
		Dispatch dispatch = new Dispatch();
		dispatch.setTaskinstId(taskinstId);
		dispatch.setUserId(userId);
		dispatch.setIsEnabled(1);
		SessionContext.getTableSession().save(dispatch);
	}
	
	/**
	 * 删除所有(运行)抄送信息
	 */
	public void deleteAllCC() {
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_cc where taskinst_id=?", Arrays.asList(taskinstId));
	}
	/**
	 * 删除所有(运行)指派信息
	 */
	public void deleteAllAssignee() {
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_assignee where taskinst_id=?", Arrays.asList(taskinstId));
	}
	/**
	 * 删除所有(运行)调度权限
	 */
	public int deleteAllDispatch() {
		return SessionContext.getSqlSession().executeUpdate("delete bpm_ru_dispatch where taskinst_id=?", Arrays.asList(taskinstId));
	}
	
	/**
	 * 设置任务被全部认领
	 */
	public void setAllClaimed() {
		SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_all_claimed=1 where taskinst_id=?", Arrays.asList(taskinstId));
	}
	/**
	 * 设置任务没有被全部认领
	 */
	public void setNotAllClaimed() {
		SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_all_claimed=null where taskinst_id=?", Arrays.asList(taskinstId));
	}
	/**
	 * 是否全部认领
	 * @return
	 */
	public boolean isAllClaimed() {
		return isAllClaimed != null && isAllClaimed == 1;
	}
	/**
	 * 是否处于活动状态
	 * @return
	 */
	public boolean isActive() {
		return suspendTime == null;
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
	public Integer getAssignCount() {
		return assignCount;
	}
	public void setAssignCount(Integer assignCount) {
		this.assignCount = assignCount;
	}
	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
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
	public Date getSuspendTime() {
		return suspendTime;
	}
	public void setSuspendTime(Date suspendTime) {
		this.suspendTime = suspendTime;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getIsAllClaimed() {
		return isAllClaimed;
	}
	public void setIsAllClaimed(Integer isAllClaimed) {
		this.isAllClaimed = isAllClaimed;
	}
}
