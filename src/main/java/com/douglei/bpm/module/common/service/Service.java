package com.douglei.bpm.module.common.service;

/**
 * 
 * @author DougLei
 */
public abstract class Service {
	
	/**
	 * 执行验证数据
	 * @param obj 被验证的实例
	 * @param validators 验证器
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected final ExecutionResult validate(Object obj, Validator...validators) {
		if(validators.length > 0) {
			ExecutionResult result = null;
			for (Validator validator : validators) {
				if((result = validator.validate(obj)) != null)
					return result;
			}
		}
		return null;
	}
}
