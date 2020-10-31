package com.douglei.bpm.module.common.service;

import com.douglei.orm.mapping.metadata.validator.ValidationResult;

/**
 * 执行结果
 * @author DougLei
 */
public class ExecutionResult extends ValidationResult{
	
	public ExecutionResult(String name, String originMessage, String code, Object... params) {
		super(name, originMessage, code, params);
	}
}