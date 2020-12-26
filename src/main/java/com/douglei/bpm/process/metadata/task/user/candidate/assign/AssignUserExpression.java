package com.douglei.bpm.process.metadata.task.user.candidate.assign;

import java.io.Serializable;

/**
 * 指派用户的表达式
 * @author DougLei
 */
public class AssignUserExpression implements Serializable {
	private String name; // 表达式名称
	private String value; // 具体的表达式值
	private String extendValue; // 扩展值
	
	public AssignUserExpression(String name, String value, String extendValue) {
		this.name = name;
		this.value = value;
		this.extendValue = extendValue;
	}

	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public String getExtendValue() {
		return extendValue;
	}
}
