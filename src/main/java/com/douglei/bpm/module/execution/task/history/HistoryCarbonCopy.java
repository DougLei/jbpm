package com.douglei.bpm.module.execution.task.history;

import java.util.Date;

import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.task.runtime.CarbonCopy;

/**
 * 
 * @author DougLei
 */
public class HistoryCarbonCopy extends CarbonCopy{
	private Date viewTime;
	private SourceType sourceType;

	public Date getViewTime() {
		return viewTime;
	}
	public void setViewTime(Date viewTime) {
		this.viewTime = viewTime;
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
