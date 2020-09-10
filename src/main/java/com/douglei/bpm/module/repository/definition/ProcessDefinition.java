package com.douglei.bpm.module.repository.definition;

import com.douglei.bpm.module.common.entity.BasicEntity;

/**
 * 
 * @author DougLei
 */
class ProcessDefinition extends BasicEntity{
	private int refTypeId;
	private String name;
	private String code;
	private String version;
	private int subversion;
	private String content;
	private int state;
	private String description;
	
	public int getRefTypeId() {
		return refTypeId;
	}
	public void setRefTypeId(int refTypeId) {
		this.refTypeId = refTypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getSubversion() {
		return subversion;
	}
	public void setSubversion(int subversion) {
		this.subversion = subversion;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}