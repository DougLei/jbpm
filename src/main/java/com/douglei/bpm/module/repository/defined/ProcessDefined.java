package com.douglei.bpm.module.repository.defined;

import com.douglei.bpm.module.common.Entity;

/**
 * 流程定义实体
 * @author DougLei
 */
public class ProcessDefined extends Entity{
	private int refTypeId;
	private int mode;
	private String code;
	private String version;
	private int version_;
	private String name;
	private int enabled;
	private String description;
	
	public int getRefTypeId() {
		return refTypeId;
	}
	public void setRefTypeId(int refTypeId) {
		this.refTypeId = refTypeId;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getVersion_() {
		return version_;
	}
	public void setVersion_(int version_) {
		this.version_ = version_;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
