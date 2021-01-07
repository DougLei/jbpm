package com.douglei.bpm.process.metadata.task.user.candidate.assign;

import java.io.Serializable;

/**
 * 可指派的人数的表达式
 * @author DougLei
 */
public class AssignNumber implements Serializable{
	private int number;
	private boolean percent; // 是否是百分比
	private Boolean ceiling; // 是否向上取整; false表示向下取整; null表示0.5<向下取整, >=0.5向上取整
	
	public AssignNumber(int number, boolean percent, Boolean ceiling) {
		this.number = number;
		this.percent = percent;
		this.ceiling = ceiling;
	}
	
	public int getNumber() {
		return number;
	}
	public boolean isPercent() {
		return percent;
	}
	public Boolean isCeiling() {
		return ceiling;
	}

	@Override
	public String toString() {
		return "AssignNumber [number=" + number + ", percent=" + percent + ", ceiling=" + ceiling + "]";
	}
}
