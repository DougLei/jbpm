package com.douglei.bpm.module.repository.type;

import java.util.List;

/**
 * 流程类型
 * @author DougLei
 */
public class ProcessType {
	private int id;
	private int parentId;
	private String code;
	private String name;
	private String tenantId;
	private List<ProcessType> children;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
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
	public List<ProcessType> getChildren() {
		return children;
	}
	public void setChildren(List<ProcessType> children) {
		this.children = children;
	}
}
