package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import java.io.Serializable;

/**
 * 串行办理策略
 * @author DougLei
 */
public class SerialHandlePolicyEntity implements Serializable{
	private String name; // 串行办理时的办理顺序策略名称
	
	public SerialHandlePolicyEntity(String name) {
		this.name = name;
	}

	/**
	 * 获取串行办理时的办理顺序策略名称
	 * @return
	 */
	public String getName() {
		return name;
	}
}
