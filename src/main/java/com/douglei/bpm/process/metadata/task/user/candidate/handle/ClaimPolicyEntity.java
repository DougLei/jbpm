package com.douglei.bpm.process.metadata.task.user.candidate.handle;

/**
 * 认领策略
 * @author DougLei
 */
public class ClaimPolicyEntity {
	private String name; // 策略名称
	private String value; // 策略需要的值
	
	public ClaimPolicyEntity(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * 获取认领策略的名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * 获取认领策略需要的值
	 * @return
	 */
	public String getValue() {
		return value;
	}
}
