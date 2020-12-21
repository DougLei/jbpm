package com.douglei.bpm.module.history.task;

import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.handler.components.variable.VariableEntity;

/**
 * 
 * @author DougLei
 */
public class HistoryVariable extends Variable{

	public HistoryVariable() {}
	public HistoryVariable(String procinstId, Integer taskId, VariableEntity variableEntity) {
		super(procinstId, taskId, variableEntity);
	}
}
