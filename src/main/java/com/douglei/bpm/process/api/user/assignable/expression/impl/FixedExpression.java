package com.douglei.bpm.process.api.user.assignable.expression.impl;

import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionParameter;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.bean.factory.UserBeanFactory;

/**
 * 指派固定的用户
 * @author DougLei
 */
@Bean(clazz=AssignableUserExpression.class)
public class FixedExpression implements AssignableUserExpression {

	@Autowired
	private UserBeanFactory userBeanFactory;
	
	@Override
	public String getName() {
		return "fixed";
	}

	@Override
	public List<UserBean> getAssignUserList(String value, String extendValue, AssignableUserExpressionParameter parameter) {
		return userBeanFactory.create(value.split(","));
	}
}
