package com.douglei.bpm.module.common;

import com.douglei.i18n.Message;

/**
 * 
 * @author DougLei
 */
public abstract class Service {
	
	/**
	 * 验证数据
	 * @param obj 被验证的实例
	 * @param validators 验证器
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected final Message validate(Object obj, Validator...validators) {
		if(validators.length > 0) {
			Message message = null;
			for (Validator validator : validators) {
				if((message = validator.validate(obj)) != null)
					return message;
			}
		}
		return null;
	}
}
