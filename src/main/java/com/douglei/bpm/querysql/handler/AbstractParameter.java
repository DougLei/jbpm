package com.douglei.bpm.querysql.handler;

import com.douglei.bpm.querysql.metadata.QuerySqlMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractParameter {
	protected LogicalOperator next = LogicalOperator.AND; // 与下一个参数之间的逻辑运算符, 默认为and
	
	/**
	 * 设置与下一个条件之间的逻辑运算符
	 * @param next
	 * @return
	 */
	public AbstractParameter setNext(LogicalOperator next) {
		this.next = next;
		return this;
	}
	
	/**
	 * 装配sql
	 * @param entity
	 * @param metadata
	 * @throws QuerySqlAssembleException
	 */
	protected abstract void assembleSQL(QuerySqlEntity entity, QuerySqlMetadata metadata) throws QuerySqlAssembleException;
}
