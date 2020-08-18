package com.douglei.bpm.module;

import com.douglei.orm.core.metadata.validator.ValidationResult;

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
	protected final ValidationResult validate(Object obj, Validator...validators) {
		if(validators.length > 0) {
			ValidationResult result = null;
			for (Validator validator : validators) {
				if((result = validator.validate(obj)) != null)
					return result;
			}
		}
		return null;
	}
}
