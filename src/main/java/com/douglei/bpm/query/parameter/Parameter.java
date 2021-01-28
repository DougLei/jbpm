package com.douglei.bpm.query.parameter;

import com.douglei.bpm.query.extend.mapping.metadata.Operator;

/**
 * (单个)参数
 * @author DougLei
 */
public class Parameter extends AbstractParameter{
	private Operator operator; // 运算符
	private String columnName; // 参与运算的列名
	private Object[] values; // 参与运算的值
	
	/**
	 * 
	 * @param operator 运算符 
	 * @param columnName 参与运算的列名
	 * @param values 参与运算的值
	 */
	public Parameter(Operator operator, String columnName, Object... values) {
		this.operator = operator;
		this.columnName = columnName;
		this.values = values;
	}
}
