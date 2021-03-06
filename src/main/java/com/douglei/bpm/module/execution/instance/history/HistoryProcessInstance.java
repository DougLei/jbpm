package com.douglei.bpm.module.execution.instance.history;

import java.util.Date;

import com.douglei.bpm.bean.PropertyValueCopier;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstance;

/**
 * 
 * @author DougLei
 */
public class HistoryProcessInstance extends ProcessInstance{
	private String userId;
	private String reason;
	private Date endTime;
	
	public HistoryProcessInstance() {}
	public HistoryProcessInstance(ProcessInstance processInstance, State state, String userId, String reason) {
		PropertyValueCopier.copy(processInstance, this);
		this.state = state;
		this.userId = userId;
		this.reason = reason;
		this.endTime = new Date();
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
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
