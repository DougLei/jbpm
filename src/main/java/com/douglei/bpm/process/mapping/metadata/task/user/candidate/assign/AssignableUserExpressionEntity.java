package com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign;

import java.io.Serializable;

/**
 * 可指派的用户表达式实体
 * @author DougLei
 */
public class AssignableUserExpressionEntity implements Serializable {
	private String name; // 表达式名称
	private String value; // 具体的表达式值
	
	public AssignableUserExpressionEntity(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
}
