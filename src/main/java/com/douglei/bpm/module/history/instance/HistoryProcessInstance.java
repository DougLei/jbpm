package com.douglei.bpm.module.history.instance;

import java.util.Date;

import com.douglei.bpm.bean.PropertyValueCopier;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;

/**
 * 
 * @author DougLei
 */
public class HistoryProcessInstance extends ProcessInstance{
	private Date endTime;
	private String remark;
	
	public HistoryProcessInstance(ProcessInstance processInstance, String remark) {
		PropertyValueCopier.copy(processInstance, this);
		this.endTime = new Date();
		this.remark = remark;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
