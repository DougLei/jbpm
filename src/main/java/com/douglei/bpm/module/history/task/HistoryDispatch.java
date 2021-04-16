package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.module.history.SourceType;
import com.douglei.bpm.module.runtime.task.Dispatch;

/**
 * 
 * @author DougLei
 */
public class HistoryDispatch extends Dispatch{
	private Date dispatchTime;
	private SourceType sourceType;

	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public int getSourceType() {
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
