package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.Assignee;
import com.douglei.bpm.module.runtime.task.HandleState;

/**
 * 
 * @author DougLei
 */
public class HistoryAssignee extends Assignee{
	private Attitude attitude;
	private String suggest;
	private Date finishTime;
	
	/**
	 * 完结指派信息
	 * @param attitude
	 * @param suggest
	 * @param finishTime
	 */
	public void finish(Attitude attitude, String suggest, Date finishTime) {
		this.attitude = attitude;
		this.suggest = suggest;
		this.finishTime = finishTime;
		super.handleState = HandleState.FINISHED;
	}
	
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
