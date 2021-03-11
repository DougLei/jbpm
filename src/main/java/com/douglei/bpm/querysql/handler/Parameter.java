package com.douglei.bpm.querysql.handler;

import com.douglei.bpm.querysql.metadata.Operator;
import com.douglei.bpm.querysql.metadata.ParameterMetadata;
import com.douglei.bpm.querysql.metadata.QuerySqlMetadata;

/**
 * 
 * @author DougLei
 */
public class Parameter extends AbstractParameter{
	private Operator operator; // 运算符
	private String name; // 参与运算的参数名
	private Object[] values; // 参与运算的值
	
	/**
	 * 
	 * @param negate 是否取反 
	 * @param operator 运算符
	 * @param name 参与运算的参数名
	 * @param values 参与运算的值
	 */
	public Parameter(boolean negate, Operator operator, String name, Object... values) {
		this.operator = (negate?operator.getNegation():operator).optimizing(values); // 处理取反, 并优化运算符
		this.name = name.toUpperCase();
		this.values = values;
	}

	@Override
	protected void assembleSQL(QuerySqlEntity entity, QuerySqlMetadata metadata) throws QuerySqlAssembleException {
		ParameterMetadata parameterMetadata = metadata.getParameterMap().get(name);
		if(parameterMetadata == null)
			throw new QuerySqlAssembleException("装配code为["+metadata.getCode()+"]的query-sql时, 传入了不存在的参数["+name+"]");
		if(!parameterMetadata.getDataType().support(operator))
			throw new QuerySqlAssembleException("装配code为["+metadata.getCode()+"]的query-sql时, 参数["+name+"]不支持["+operator.name()+"]运算");
		
		operator.assembleSQL(name, values, entity);
	}
}
