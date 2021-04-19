package com.douglei.bpm.process.mapping.metadata.gateway;

import com.douglei.bpm.process.mapping.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractGatewayMetadata extends TaskMetadata {
	private boolean[] variableExtend; // 流程变量继承范围
	
	protected AbstractGatewayMetadata(String id, String name, String defaultOutputFlowId, boolean[] variableExtend) {
		super(id, name, defaultOutputFlowId);
		this.variableExtend = variableExtend;
	}

	public boolean[] getVariableExtend() {
		return variableExtend;
	}
}
