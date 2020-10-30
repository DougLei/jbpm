package com.douglei.bpm.module.common.service;

/**
 * 验证器
 * @author DougLei
 */
public interface Validator<T> {
	
	/**
	 * 执行验证
	 * @param t
	 * @return 如果验证通过, 返回null
	 */
	ExecutionResult validate(T t);
}
