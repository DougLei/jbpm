package com.douglei.bpm.module.common;

/**
 * 验证器
 * @author DougLei
 */
public interface Validator<T> {
	
	/**
	 * 执行验证
	 * @param t
	 * @return
	 */
	ExecutionResult validate(T t);
}
