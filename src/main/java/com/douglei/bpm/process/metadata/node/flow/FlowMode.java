package com.douglei.bpm.process.metadata.node.flow;

import com.douglei.tools.utils.StringUtil;

/**
 * (多条)流的模式
 * @author DougLei
 */
public enum FlowMode {
	EXCLUSIVE, // 排他
	PARALLEL, // 并行
	INCLUSIVE; // 包容

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
