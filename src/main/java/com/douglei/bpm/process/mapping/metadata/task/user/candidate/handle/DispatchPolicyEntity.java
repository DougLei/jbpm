package com.douglei.bpm.process.mapping.metadata.task.user.candidate.handle;

import com.douglei.orm.mapping.metadata.Metadata;

/**
 * 调度策略
 * @author DougLei
 */
public class DispatchPolicyEntity implements Metadata{
	private String name; // 策略名称
	
	public DispatchPolicyEntity(String name) {
		this.name = name;
	}

	/**
	 * 获取调度策略名称
	 * @return
	 */
	public String getName() {
		return name;
	}
}
