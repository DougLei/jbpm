package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.Dispatch;

/**
 * 
 * @author DougLei
 */
public class HistoryDispatch extends Dispatch{
	private Date dispatchTime;

	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
}
