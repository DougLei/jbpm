package com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign;

import java.io.Serializable;

import com.douglei.orm.mapping.metadata.Metadata;

/**
 * 最多可指派的人数的表达式
 * @author DougLei
 */
public class AssignNumber implements Metadata{
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
	
	/**
	 * 计算上限
	 * @param totalCount (可有的)总数量
	 * @return
	 */
	public int calcUpperLimit(int totalCount) {
		if(percent) {
			int upperLimit = totalCount*number;
			if(upperLimit%100>0 && ceiling) {
				upperLimit = upperLimit/100+1;
			}else {
				upperLimit = upperLimit/100;
			}
			if(upperLimit == 0)
				return 1;
			return upperLimit;
		}
		
		if(number > totalCount)
			return totalCount;
		return number;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [number=" + number + ", percent=" + percent + ", ceiling=" + ceiling + "]";
	}
}
