package com.douglei.bpm.module.history.task;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.CarbonCopy;

/**
 * 
 * @author DougLei
 */
public class HistoryCarbonCopy extends CarbonCopy{
	private Date readTime;

	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
}
