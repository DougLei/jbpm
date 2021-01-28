package com.douglei.bpm.query;

import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.query.parameter.AbstractParameter;

/**
 * 
 * @author DougLei
 */
@Bean
public class QuerySqlAssembler {
	
	/**
	 * 装配查询sql实体
	 * @param name sql名
	 * @param parameters 参数集合
	 * @return
	 * @throws QuerySqlAssembleException
	 */
	public QuerySqlEntity assembling(String name, List<AbstractParameter> parameters) throws QuerySqlAssembleException{
		// TODO 
		
		return null;
	}
}
