package com.douglei.bpm.querysql.handler;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.querysql.metadata.QuerySqlMetadata;

/**
 *
 * @author DougLei
 */
public class ParameterGroup extends AbstractParameter{
	private List<AbstractParameter> parameters;

	/**
	 * 添加参数
	 * @param parameter
	 * @return
	 */
	public ParameterGroup addParameter(Parameter parameter) {
		if(parameters == null)
			parameters = new ArrayList<AbstractParameter>();
		parameters.add(parameter);
		return this;
	}

	@Override
	protected void assembleSQL(QuerySqlEntity entity, QuerySqlMetadata metadata) throws QuerySqlAssembleException {
		if(parameters == null)
			return;
		
		entity.appendConditionSQLLeftParenthesis();
		for(int i=0;i<parameters.size();i++) {
			parameters.get(i).assembleSQL(entity, metadata);
			if(i < parameters.size()-1)
				entity.appendConditionSQLNext(parameters.get(i).next);
		}
		entity.appendConditionSQLRightParenthesis();
	}
}
