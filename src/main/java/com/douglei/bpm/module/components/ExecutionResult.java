package com.douglei.bpm.module.components;

import com.douglei.i18n.I18nContext;
import com.douglei.i18n.Result;

/**
 * 执行结果
 * @author DougLei
 */
public class ExecutionResult<T> extends Result{
	private boolean success; // 结果是否成功
	private T object; // 当结果成功时, 可通过该属性传递某个实例
	
	public ExecutionResult(String originMessage, String code, Object... params) {
		super(originMessage, code, params);
	}
	public ExecutionResult(T object) {
		this.object = object;
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
	
	/**
	 * 获取执行结果为false时的具体(国际化)消息
	 * @return
	 */
	public String getFailMessage() {
		if(success)
			return null;
		return I18nContext.getMessage(this).getMessage();
	}
	
	/**
	 * 当执行结果为false时, 可通过该方法进行执行结果的泛型转换
	 * @param targetClass
	 * @return
	 */
	public <P> ExecutionResult<P> convertGenericsOnFail(Class<P> targetClass) {
		if(success)
			throw new IllegalArgumentException("当前执行结果为true时, 无法调用 convertGenericsOnFail(Class) 方法");
		
		if(getParams().length == 0)
			return new ExecutionResult<P>(getOriginMessage(), getCode());
		return new ExecutionResult<P>(getOriginMessage(), getCode(), getParams());
	}
	
	@Override
	public String toString() {
		if(success)
			return getClass().getSimpleName() + " [success=true, object=" + object +"]";
		return getClass().getSimpleName() + " [success=false, failMessage="+getFailMessage()+"]";
	}
}