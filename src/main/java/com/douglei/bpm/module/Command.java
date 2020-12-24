package com.douglei.bpm.module;

import com.douglei.bpm.bean.BeanInstances;

/**
 * 
 * @author DougLei
 */
public interface Command {

	/**
	 * 
	 * @param beanInstances
	 * @return
	 */
	ExecutionResult execute(BeanInstances beanInstances);
}
