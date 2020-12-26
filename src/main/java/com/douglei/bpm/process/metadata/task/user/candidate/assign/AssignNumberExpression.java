package com.douglei.bpm.process.metadata.task.user.candidate.assign;

import java.io.Serializable;

/**
 * 可指派的人数的表达式
 * @author DougLei
 */
public class AssignNumberExpression implements Serializable{
	private int number;
	private boolean percent; // 是否是百分比
	private Boolean ceiling; // 是否向上取整
	
	public AssignNumberExpression(int number, boolean percent, Boolean ceiling) {
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
}
