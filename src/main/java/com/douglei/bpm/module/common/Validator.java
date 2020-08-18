package com.douglei.bpm.module.common;

import com.douglei.i18n.Message;

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
	Message validate(T t);
}
