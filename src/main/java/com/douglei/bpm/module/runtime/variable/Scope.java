package com.douglei.bpm.module.runtime.variable;

/**
 * 变量的范围
 * @author DougLei
 */
public enum Scope {
	GLOBAL,
	LOCAL,
	TRANSIENT;
	
	private String name;
	private Scope() {
		this.name = name().toLowerCase();
	}
	public String getName() {
		return name;
	}
	
	/**
	 * 根据字符串值获取对应的范围枚举实例
	 * @param str
	 * @return
	 */
	public static Scope getByString(String str) {
		if(GLOBAL.name.equals(str))
			return GLOBAL;
		if(LOCAL.name.equals(str))
			return LOCAL;
		if(TRANSIENT.name.equals(str))
			return TRANSIENT;
		throw new NullPointerException("不存在值为["+str+"]的变量范围");
	}
}
