package com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle;

import com.douglei.orm.mapping.metadata.Metadata;

/**
 * 串行办理策略
 * @author DougLei
 */
public class SerialHandlePolicyEntity implements Metadata{
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
