package com.douglei.bpm.module.runtime.instance;

import java.util.Date;

import com.douglei.bpm.bean.annotation.Property;

/**
 * 
 * @author DougLei
 */
public class ProcessInstance {
	protected int id;
	@Property protected int procdefId;
	@Property protected String procinstId;
	@Property protected String parentProcinstId;
	@Property protected String title;
	@Property protected String businessId;
	@Property protected String pageId;
	@Property protected String startUserId;
	@Property protected Date startTime;
	@Property protected String tenantId;
	protected ProcessInstanceState state;
	
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
	public String getParentProcinstId() {
		return parentProcinstId;
	}
	public void setParentProcinstId(String parentProcinstId) {
		this.parentProcinstId = parentProcinstId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getStartUserId() {
		return startUserId;
	}
	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public ProcessInstanceState getStateInstance() {
		return state;
	}
	public void setStateInstance(ProcessInstanceState state) {
		this.state = state;
	}
	public String getState() {
		return state.name();
	}
	public void setState(String state) {
		this.state = ProcessInstanceState.valueOf(state);
	}
}
