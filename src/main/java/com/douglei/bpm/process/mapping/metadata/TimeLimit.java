package com.douglei.bpm.process.mapping.metadata;

import com.douglei.orm.mapping.metadata.Metadata;

/**
 * 
 * @author DougLei
 */
public class TimeLimit implements Metadata{
	private long time; // 限制的时间, 用毫秒存储
	private TimeLimitType type; // 时限类型
	
	public void addDays(int days) {
		time += days*24*60*60*1000;
	}
	public void addHours(int hours) {
		time += hours*60*60*1000;
	}
	public void addMinutes(int minutes) {
		time += minutes*60*1000;
	}
	public void setType(TimeLimitType type) {
		this.type = type;
	}
	
	public long getTime() {
		return time;
	}
	public TimeLimitType getType() {
		if(type == null)
			return TimeLimitType.NATURAL;
		return type;
	}
	
	@Override
	public String toString() {
		return "TimeLimit [time=" + time + ", type=" + getType() + "]";
	} 
}
