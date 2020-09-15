package com.douglei.bpm.module.common.service;

import java.util.Arrays;

import com.douglei.orm.core.metadata.validator.ValidationResult;

/**
 * 执行结果
 * @author DougLei
 */
public class ExecutionResult<T> extends ValidationResult{
	private boolean success;
	private T object;
	
	public ExecutionResult(boolean success, T object) {
		this.success = success;
		this.object = object;
	}
	public ExecutionResult(String fieldName, String originMessage, String code, Object... params) {
		super(fieldName, originMessage, code, params);
	}
	
	public boolean isSuccess() {
		return success;
	}
	public boolean isFail() {
		return !success;
	}
	public T getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		if(success)
			return "ExecutionResult [success=true, object="+object+"]";
		return "ExecutionResult [success=false, fieldName="+getFieldName()+", originMessage=" + getOriginMessage() + ", code=" + getCode() + ", params=" + Arrays.toString(getParams()) +"]";
	}
}