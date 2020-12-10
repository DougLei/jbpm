package com.douglei.bpm.module.repository.definition.entity;

/**
 * 
 * @author DougLei
 */
public enum State {
	INITIAL, // 初始化
	DEPLOY, // 部署
	UNDEPLOY, // 未部署
	INVALID, // 无效
	DELETE; // 被删除

	private String name;
	private State() {
		this.name = name().toLowerCase();
	}
	public String getName() {
		return name;
	}
	
	/**
	 * 根据字符串值获取对应的状态枚举实例
	 * @param str
	 * @return
	 */
	public static State getByString(String str) {
		if(INITIAL.name.equals(str))
			return INITIAL;
		if(DEPLOY.name.equals(str))
			return DEPLOY;
		if(UNDEPLOY.name.equals(str))
			return UNDEPLOY;
		if(INVALID.name.equals(str))
			return INVALID;
		if(DELETE.name.equals(str))
			return DELETE;
		throw new NullPointerException("不存在值为["+str+"]的流程定义状态");
	}
}
