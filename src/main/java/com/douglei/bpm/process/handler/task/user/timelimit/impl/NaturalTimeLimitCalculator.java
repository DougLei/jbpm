package com.douglei.bpm.process.handler.task.user.timelimit.impl;

import java.util.Date;

import com.douglei.bpm.process.handler.task.user.timelimit.TimeLimitCalculator;

/**
 * 
 * @author DougLei
 */
public class NaturalTimeLimitCalculator implements TimeLimitCalculator {

	@Override
	public Date getExpiryTime(Date currentDate, long times) {
		return new Date(currentDate.getTime() + times);
	}

	@Override
	public Date getExpiryTime(Date previousExpiryTime, Date startTime, Date suspendTime, Date currentDate, long times) {
		long suspendTimes = currentDate.getTime() - suspendTime.getTime(); // 挂起的时间
		return new Date(previousExpiryTime.getTime() + suspendTimes);
	}
}
