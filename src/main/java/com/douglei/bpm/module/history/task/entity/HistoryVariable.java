package com.douglei.bpm.module.history.task.entity;

import com.douglei.bpm.module.runtime.variable.ProcessVariable;
import com.douglei.bpm.module.runtime.variable.entity.Variable;

/**
 * 
 * @author DougLei
 */
public class HistoryVariable extends Variable{

	public HistoryVariable() {}
	public HistoryVariable(int procdefId, int procinstId, Integer taskId, ProcessVariable processVariable) {
		super(procdefId, procinstId, taskId, processVariable);
	}
}
