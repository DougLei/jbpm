package com.douglei.bpm.process.mapping.metadata.task.user;

import com.douglei.orm.mapping.metadata.Metadata;

/**
 * 
 * @author DougLei
 */
public class TimeLimit implements Metadata{
	private long times; // 时限(毫秒)
	private TimeLimitType type; // 时限类型
	
	public void addDays(int days) {
		times += days*24*60*60*1000;
	}
	public void addHours(int hours) {
		times += hours*60*60*1000;
	}
	public void addMinutes(int minutes) {
		times += minutes*60*1000;
	}
	public void setType(TimeLimitType type) {
		this.type = type;
	}
	
	public long getTimes() {
		return times;
	}
	public TimeLimitType getType() {
		if(type == null)
			return TimeLimitType.NATURAL;
		return type;
	}
	
	@Override
	public String toString() {
		return "TimeLimit [times=" + times + ", type=" + getType() + "]";
	} 
}
