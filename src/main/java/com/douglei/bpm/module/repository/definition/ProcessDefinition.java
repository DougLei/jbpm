package com.douglei.bpm.module.repository.definition;

import com.douglei.bpm.module.common.entity.BasicEntity;

/**
 * 
 * @author DougLei
 */
class ProcessDefinition extends BasicEntity{
	public static int UNPUBLISHED = 0; // 未发布
	public static int PUBLISHED = 1; // 已发布
	public static int DELETE = 2; // 被删除
	
	private int refTypeId;
	private String name;
	private String code;
	private String version;
	private int subversion;
	private String content;
	private String signature;
	private int state;
	private String description;
	
	public int getRefTypeId() {
		return refTypeId;
	}
	public void setRefTypeId(int refTypeId) {
		this.refTypeId = refTypeId;
	}
	public String getName() {
		if(name == null)
			name = code + ":" + version;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
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