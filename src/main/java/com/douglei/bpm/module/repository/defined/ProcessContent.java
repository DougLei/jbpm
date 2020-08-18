package com.douglei.bpm.module.repository.defined;

import com.douglei.bpm.module.Entity;

/**
 * 流程内容实体
 * @author DougLei
 */
public class ProcessContent extends Entity{
	private int refDefId;
	private int type;
	private String content;
	
	public int getRefDefId() {
		return refDefId;
	}
	public void setRefDefId(int refDefId) {
		this.refDefId = refDefId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
