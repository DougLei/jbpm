package com.douglei.bpm.process.executer.flow;

import com.douglei.tools.utils.StringUtil;

/**
 * (多条)流的模式
 * @author DougLei
 */
public enum FlowMode {
	
	/**
	 * 排他
	 */
	EXCLUSIVE,
	
	/**
	 * 并行
	 */
	PARALLEL,
	
	/**
	 * 包容
	 */
	INCLUSIVE;

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static FlowMode toValue(String value) {
		if(StringUtil.notEmpty(value)) {
			value = value.trim().toUpperCase();
			if("PARALLEL".equals(value))
				return PARALLEL;
			if("INCLUSIVE".equals(value))
				return INCLUSIVE;
		}
		return EXCLUSIVE;
	}
}
