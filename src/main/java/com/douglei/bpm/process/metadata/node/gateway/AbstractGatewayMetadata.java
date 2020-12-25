package com.douglei.bpm.process.metadata.node.gateway;

import com.douglei.bpm.process.metadata.node.MultiFlowTaskMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractGatewayMetadata extends MultiFlowTaskMetadata {
	private int unextendScopeWeight; // 不继承的流程变量范围权值和
	
	protected AbstractGatewayMetadata(String id, String name, String defaultFlowId, int unextendScopeWeight) {
		super(id, name, defaultFlowId);
		this.unextendScopeWeight = unextendScopeWeight;
	}
	public int getUnextendScopeWeight() {
		return unextendScopeWeight;
	}
}
