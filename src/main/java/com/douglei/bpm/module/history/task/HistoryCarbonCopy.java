package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.CarbonCopy;

/**
 * 
 * @author DougLei
 */
public class HistoryCarbonCopy extends CarbonCopy{
	private Date viewTime;

	public Date getViewTime() {
		return viewTime;
	}
	public void setViewTime(Date viewTime) {
		this.viewTime = viewTime;
	}
}
