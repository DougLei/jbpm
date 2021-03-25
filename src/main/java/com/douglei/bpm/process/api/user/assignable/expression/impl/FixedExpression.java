package com.douglei.bpm.process.api.user.assignable.expression.impl;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionParameter;

/**
 * 指派固定的用户
 * @author DougLei
 */
@Bean(clazz=AssignableUserExpression.class)
public class FixedExpression implements AssignableUserExpression {

	@Override
	public String getName() {
		return "fixed";
	}

	@Override
	public List<String> getUserIds(String value, AssignableUserExpressionParameter parameter) {
		String[] userIds = value.split(",");
		
		List<String> list = new ArrayList<String>(userIds.length);
		for (String userId : userIds) 
			list.add(userId);
		
		return list;
	}
}
