package com.douglei.bpm.module.history.variable;

import com.douglei.bpm.module.history.SourceType;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.handler.VariableEntity;

/**
 * 
 * @author DougLei
 */
public class HistoryVariable extends Variable{
	private SourceType sourceType;
	
	public HistoryVariable() {}
	public HistoryVariable(String procinstId, String taskinstId, VariableEntity variableEntity) {
		super(procinstId, taskinstId, variableEntity);
	}
	
	public int getSourceType() {
		return sourceType.getValue();
	}
	public void setSourceType(int sourceType) {
		this.sourceType = SourceType.valueOf(sourceType);
	}
	public SourceType getSourceTypeInstance() {
		return sourceType;
	}
	public void setSourceTypeInstance(SourceType sourceType) {
		this.sourceType = sourceType;
	}
}
