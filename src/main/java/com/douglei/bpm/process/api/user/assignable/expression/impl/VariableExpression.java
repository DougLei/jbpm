package com.douglei.bpm.process.api.user.assignable.expression.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionParameter;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.bpm.process.api.user.bean.factory.UserBeanFactory;
import com.douglei.tools.instances.ognl.OgnlHandler;

/**
 * 使用流程变量指派用户
 * @author DougLei
 */
@Bean(clazz=AssignableUserExpression.class)
public class VariableExpression implements AssignableUserExpression {
	private OgnlHandler ognlHandler = OgnlHandler.getSingleton();
	
	@Autowired
	private UserBeanFactory userBeanFactory;
	
	@Override
	public String getName() {
		return "variable";
	}

	@Override
	public List<UserBean> getAssignUserList(String value, AssignableUserExpressionParameter parameter) {
		Map<String, Object> variableMap = parameter.getCurrentTaskVariables().getVariableMap();
		if(variableMap == null)
			return null;
		
		List<String> list = new ArrayList<String>();
		Object userIds;
		for (String variableName : value.split(",")) {
			userIds = ognlHandler.getObjectValue(variableName.trim(), variableMap);
			if(userIds != null) {
				for(String userId : userIds.toString().split(",")) 
					list.add(userId.trim());
			}
		}
		return userBeanFactory.create(list);
	}
}
