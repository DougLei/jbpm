package com.douglei.bpm.process.metadata.task.user.candidate.assign;

import java.io.Serializable;

/**
 * 最多可指派的人数的表达式
 * @author DougLei
 */
public class AssignNumber implements Serializable{
	private int number;
	private boolean percent; // 是否是百分比
	private boolean ceiling; // 是否向上取整; false表示向下取整; 默认值为false
	
	public AssignNumber(int number, boolean percent, boolean ceiling) {
		this.number = number;
		this.percent = percent;
		this.ceiling = ceiling;
	}
	
	/**
	 * 获取数值
	 * @return
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * 是否是百分比
	 * @return
	 */
	public boolean isPercent() {
		return percent;
	}
	/**
	 *  是否向上取整
	 * @return
	 */
	public boolean isCeiling() {
		return ceiling;
	}

	@Override
	public String toString() {
		return "AssignNumber [number=" + number + ", percent=" + percent + ", ceiling=" + ceiling + "]";
	}
}
