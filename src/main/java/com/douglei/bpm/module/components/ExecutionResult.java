package com.douglei.bpm.module.components;

import com.douglei.i18n.Result;

/**
 * 执行结果
 * @author DougLei
 */
public class ExecutionResult<T> extends Result{
	private boolean success; // 结果是否成功
	private T object; // 可通过该属性传递某个(主要)实例
	private Object[] extendObjects; // 对object的扩展, 可通过该属性传递多个实例
	
	public ExecutionResult(String originMessage, String code, Object... params) {
		super(originMessage, code, params);
	}
	public ExecutionResult(T object, Object... extendObjects) {
		this.object = object;
		this.extendObjects = extendObjects;
		this.success = true;
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
	public Object[] getExtendObjects() {
		return extendObjects;
	}
}