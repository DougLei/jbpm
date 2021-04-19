package com.douglei.bpm.module.execution.task.history;

import java.util.Date;

import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.runtime.Assignee;

/**
 * 
 * @author DougLei
 */
public class HistoryAssignee extends Assignee {
	private Attitude attitude;
	private String suggest;
	private Date finishTime;
	private SourceType sourceType;
	
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
		this.handleState = HandleState.FINISHED;
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
	public Integer getSourceType() {
		if(sourceType == null)
			return null;
		return sourceType.getValue();
	}
	public void setSourceType(int sourceType) {
		this.sourceType = SourceType.valueOf(sourceType);
	}
	public SourceType getSourceTypeInstance() {
		return sourceType;
	}
	public void setSourceTypeInstance(SourceType sourceType) {
		this.sourceType = sourceType;
	}
}
