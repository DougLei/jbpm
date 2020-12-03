package com.douglei.bpm.module.history.task.entity;

import com.douglei.bpm.module.runtime.instance.ProcessVariable;
import com.douglei.bpm.module.runtime.task.entity.variable.Variable;

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
