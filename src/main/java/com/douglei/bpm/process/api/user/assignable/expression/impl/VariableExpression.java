package com.douglei.bpm.process.api.user.assignable.expression.impl;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpression;
import com.douglei.bpm.process.api.user.assignable.expression.AssignableUserExpressionParameter;
import com.douglei.orm.context.SessionContext;

/**
 * 使用流程变量指派用户
 * @author DougLei
 */
@Bean(clazz=AssignableUserExpression.class)
public class VariableExpression implements AssignableUserExpression {
	
	@Override
	public String getName() {
		return "variable";
	}

	@Override
	public List<String> getUserIds(String value, AssignableUserExpressionParameter parameter) {
		VariableQuerier variableQuerier = new VariableQuerier(parameter.getProcinstId(), parameter.getTaskinstId(), value.split(","));
		if(variableQuerier.variables.isEmpty())
			return null;
		
		List<String> list = new ArrayList<String>();
		for(Variable variable: variableQuerier.variables){
			Object val = variable.getValue();
			if(val == null)
				continue;
			
			for(String userId : val.toString().split(",")) 
				list.add(userId.trim());
		}
		return list;
	}
	
	/**
	 * 变量查询器
	 * @author DougLei
	 */
	private class VariableQuerier{
		public List<Variable> variables;
 		
		public VariableQuerier(String procinstId, String taskinstId, String... variableNames) {
			// 初始化SQL
			StringBuilder sql = new StringBuilder("select * from bpm_ru_variable where procinst_id=? and (taskinst_id=? or scope =?) and name");
			
			// 初始化参数
			List<Object> parameters = new ArrayList<Object>(3+variableNames.length);
			parameters.add(procinstId);
			parameters.add(taskinstId);
			parameters.add(Scope.GLOBAL.name());
			
			// 动态装配条件
			if(variableNames.length == 1) {
				sql.append("=?");
				parameters.add(variableNames[0]);
			}else {
				sql.append(" in (");
				for (String variableName : variableNames) {
					sql.append("?,");
					parameters.add(variableName);
				}
				sql.setLength(sql.length()-1);
				sql.append(')');
			}
			
			// 执行查询
			this.variables = SessionContext.getTableSession().query(Variable.class, sql.toString(), parameters);
		}
	}
}