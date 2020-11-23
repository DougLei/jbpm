package com.douglei.bpm.module.components.command.context.transaction;

import com.douglei.orm.configuration.environment.datasource.TransactionIsolationLevel;

/**
 * 事物配置
 * @author DougLei
 */
public interface TransactionConfig {
	TransactionConfig DEFAULT_CONFIG = new TransactionConfig(){};
	
	/**
	 * 传播行为
	 * @return
	 */
	default PropagationBehavior propagationBehavior() {
		return PropagationBehavior.REQUIRED;
	}
	
	/**
	 * 是否开启事物
	 * @return
	 */
	default boolean beginTransaction() {
		return true;
	}
	
	/**
	 * 事物隔离级别
	 * @return
	 */
	default TransactionIsolationLevel transactionIsolationLevel() {
		return TransactionIsolationLevel.DEFAULT;
	}
}
