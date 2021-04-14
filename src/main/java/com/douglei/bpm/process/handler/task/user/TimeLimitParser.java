package com.douglei.bpm.process.handler.task.user;

import java.util.Date;

import com.douglei.bpm.process.mapping.metadata.task.user.TimeLimit;

/**
 * 时限解析器
 * @author DougLei
 */
public class TimeLimitParser {
	private Date expiryTime;
	
	TimeLimitParser(Date currentDate, TimeLimit timeLimit) {
		// TODO 目前只实现自然日
		this.expiryTime = new Date(currentDate.getTime() + timeLimit.getTime());
	}

	/**
	 * 获取截止日期
	 * @return
	 */
	public Date getExpiryTime() {
		return expiryTime;
	}
}
