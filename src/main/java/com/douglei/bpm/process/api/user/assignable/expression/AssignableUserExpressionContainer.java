package com.douglei.bpm.process.api.user.assignable.expression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.CustomAutowired;
import com.douglei.bpm.bean.annotation.Bean;

/**
 * 可指派的用户表达式容器
 * @author DougLei
 */
@Bean
public class AssignableUserExpressionContainer implements CustomAutowired{
	private Map<String, AssignableUserExpression> map = new HashMap<String, AssignableUserExpression>();
	
	@SuppressWarnings("unchecked")
	@Override
	public void setFields(Map<Class<?>, Object> beanContainer) {
		((List<AssignableUserExpression>)beanContainer.get(AssignableUserExpression.class)).forEach(expression -> {
			map.put(expression.getName(), expression);
		});
	}
	
	/**
	 * 获取指定name的表达式实例
	 * @param name
	 * @return
	 */
	public AssignableUserExpression get(String name) {
		return map.get(name);
	}
}
