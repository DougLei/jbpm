package com.douglei.bpm.module.repository.type;

import com.douglei.bpm.module.common.entity.BasicEntity;

/**
 * 流程类型实体
 * @author DougLei
 */
public class ProcessType extends BasicEntity{
	private String code;
	private String name;

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
