package com.douglei.bpm.query.parameter;

import com.douglei.bpm.query.parameter.op.LogicalOperator;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractParameter {
	protected LogicalOperator nlo = LogicalOperator.AND; // 与下一个参数之间的逻辑运算符, 默认为and
	
	/**
	 * 设置与下一个条件之间的逻辑运算符
	 * @param nlo
	 * @return
	 */
	public AbstractParameter setNextLogicalOperator(LogicalOperator nlo) {
		this.nlo = nlo;
		return this;
	}
}
