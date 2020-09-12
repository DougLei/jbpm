package com.douglei.bpm.module.common.service;

import com.douglei.orm.core.metadata.validator.ValidationResult;

/**
 * 执行结果
 * @author DougLei
 */
public class ExecutionResult extends ValidationResult{
	private boolean success;
	private Object object;
	
	public ExecutionResult(boolean success, Object object) {
		this.success = success;
		this.object = object;
	}
	public ExecutionResult(String fieldName, String originMessage, String code, Object... params) {
		super(fieldName, originMessage, code, params);
	}
	
	public boolean isSuccess() {
		return success;
	}
	public Object getObject() {
		return object;
	}
}
