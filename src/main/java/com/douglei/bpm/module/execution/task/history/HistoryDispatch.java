package com.douglei.bpm.module.execution.task.history;

import java.util.Date;

import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.task.runtime.Dispatch;

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
	public Integer getSourceType() {
		if(sourceType == null)
			return null;
		return sourceType.getValue();
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = SourceType.valueOf(sourceType);
	}
	public SourceType getSourceTypeInstance() {
		return sourceType;
	}
	public void setSourceTypeInstance(SourceType sourceType) {
		this.sourceType = sourceType;
	}
}
