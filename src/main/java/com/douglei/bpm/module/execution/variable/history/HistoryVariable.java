package com.douglei.bpm.module.execution.variable.history;

import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.variable.runtime.Variable;
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
		this.sourceType = SourceType.STANDARD;
	}
	
	public Integer getSourceType() {
		if(sourceType == null)
			return null;
		return sourceType.getValue();
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = SourceType.valueOf(sourceType);
	}
	public SourceType getSourceTypeInstance() {
		return sourceType;
	}
	public void setSourceTypeInstance(SourceType sourceType) {
		this.sourceType = sourceType;
	}
}
