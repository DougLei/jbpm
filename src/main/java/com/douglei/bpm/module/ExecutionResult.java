package com.douglei.bpm.module;

import com.douglei.orm.core.metadata.validator.ValidationResult;

/**
 * 
 * @author DougLei
 */
public class ExecutionResult extends ValidationResult{

	public ExecutionResult(String fieldName, String originMessage, String code, Object... params) {
		super(fieldName, originMessage, code, params);
	}
}
