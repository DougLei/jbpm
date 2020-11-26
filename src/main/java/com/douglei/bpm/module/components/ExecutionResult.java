package com.douglei.bpm.module.components;

import java.util.Arrays;

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
	
	/**
	 * 当执行结果为false时, 可通过该方法进行执行结果的泛型转换
	 * @param targetClass
	 * @return
	 */
	public <P> ExecutionResult<P> convertGenericsOnFail(Class<P> targetClass) {
		if(success)
			throw new IllegalArgumentException("当前执行结果为true时, 禁止调用 convertGenericsOnFail(Class) 方法");
		
		if(getParams().length == 0)
			return new ExecutionResult<P>(getOriginMessage(), getCode());
		return new ExecutionResult<P>(getOriginMessage(), getCode(), getParams());
	}
	
	@Override
	public String toString() {
		if(success)
			return getClass().getSimpleName() + " [success=true, object=" + object + ", extendObjects=" + Arrays.toString(extendObjects) +"]";
		return getClass().getSimpleName() + " [success=false, originMessage=" + getOriginMessage() + ", code=" + getCode() + ", params=" + Arrays.toString(getParams()) +"]";
	}
}