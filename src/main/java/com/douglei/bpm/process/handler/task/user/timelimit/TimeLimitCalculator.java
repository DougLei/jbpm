package com.douglei.bpm.process.handler.task.user.timelimit;

import java.util.Date;

/**
 * 时限计算器
 * @author DougLei
 */
public interface TimeLimitCalculator {
	
	/**
	 * (启动任务时)计算并获取截止日期
	 * @param currentDate 当前时间
	 * @param times 时限(毫秒)
	 * @return
	 */
	Date getExpiryTime(Date currentDate, long times);

	/**
	 * (唤醒任务时)计算并获取截止日期
	 * @param previousExpiryTime 之前的截止日期
	 * @param startTime 任务的启动时间
	 * @param suspendTime 任务的挂起时间
	 * @param currentDate 当前时间
	 * @param times 时限(毫秒)
	 * @return
	 */
	Date getExpiryTime(Date previousExpiryTime, Date startTime, Date suspendTime, Date currentDate, long times);
}
