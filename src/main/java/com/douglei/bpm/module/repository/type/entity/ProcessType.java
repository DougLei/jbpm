package com.douglei.bpm.module.repository.type.entity;

/**
 * 流程类型实体
 * @author DougLei
 */
public class ProcessType {
	private int id;
	private String code;
	private String name;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
