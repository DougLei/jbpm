package com.douglei.bpm.query;

import java.util.List;
import java.util.Map;

import com.douglei.bpm.query.extend.mapping.metadata.Operator;
import com.douglei.bpm.query.extend.mapping.metadata.ParameterStandardMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractParameter {
	protected LogicalOperator nlo = LogicalOperator.AND; // 与下一个参数之间的逻辑运算符, 默认为and
	
	/**
	 * 设置与下一个条件之间的逻辑运算符
	 * @param nlo
	 * @return
	 */
	public AbstractParameter setNextLogicalOperator(LogicalOperator nlo) {
		this.nlo = nlo;
		return this;
	}
	
	/**
	 * 装配sql
	 * @param sql 
	 * @param parameters 
	 * @param counter 计数器, 记录参数使用操作的次数
	 * @param psmMap 
	 * @throws QuerySqlAssembleException
	 */
	protected abstract void assembleSQL(StringBuilder sql, List<Object> parameters, Map<String, Map<Operator, Integer>> counter, Map<String, ParameterStandardMetadata> psmMap) throws QuerySqlAssembleException;
}
