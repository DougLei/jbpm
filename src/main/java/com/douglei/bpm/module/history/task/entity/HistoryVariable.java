package com.douglei.bpm.module.history.task.entity;

import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.module.runtime.variable.VariableEntity;

/**
 * 
 * @author DougLei
 */
public class HistoryVariable extends Variable{

	public HistoryVariable() {}
	public HistoryVariable(int procdefId, int procinstId, Integer taskId, VariableEntity processVariable) {
		super(procdefId, procinstId, taskId, processVariable);
	}
}
