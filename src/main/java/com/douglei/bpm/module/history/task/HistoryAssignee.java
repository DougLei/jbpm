package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.Assignee;

/**
 * 
 * @author DougLei
 */
public class HistoryAssignee extends Assignee{
	private Attitude attitude;
	private String suggest;
	private Date finishTime;
	
	public Attitude getAttitudeInstance() {
		return attitude;
	}
	public void setAttitudeInstance(Attitude attitude) {
		this.attitude = attitude;
	}
	public String getAttitude() {
		if(attitude == null)
			return null;
		return attitude.name();
	}
	public void setAttitude(String attitude) {
		if(attitude == null)
			return;
		this.attitude = Attitude.valueOf(attitude);
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
}
