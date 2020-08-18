package com.douglei.bpm.module;

import com.douglei.orm.core.metadata.validator.ValidationResult;

/**
 * 验证器
 * @author DougLei
 */
public interface Validator<T> {
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	ValidationResult validate(T t);
}
