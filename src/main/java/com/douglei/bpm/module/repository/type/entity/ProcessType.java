package com.douglei.bpm.module.repository.type.entity;

/**
 * 流程类型实体
 * @author DougLei
 */
public class ProcessType {
	private int id;
	private String code;
	private String name;
	private String tenantId;

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
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
