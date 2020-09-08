package com.douglei.bpm.module.common.service;

import com.douglei.orm.core.metadata.validator.ValidationResult;

/**
 * 执行结果
 * @author DougLei
 */
public class ExecutionResult extends ValidationResult{
	
	public ExecutionResult(String fieldName, String originMessage, String code, Object... params) {
		super(fieldName, originMessage, code, params);
	}
}
