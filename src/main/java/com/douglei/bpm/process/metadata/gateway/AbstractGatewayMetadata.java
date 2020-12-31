package com.douglei.bpm.process.metadata.gateway;

import com.douglei.bpm.process.metadata.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractGatewayMetadata extends TaskMetadata {
	private int unextendScopeWeight; // 不继承的流程变量范围权值和
	
	protected AbstractGatewayMetadata(String id, String name, int unextendScopeWeight) {
		super(id, name);
		this.unextendScopeWeight = unextendScopeWeight;
	}
	
	public final int getUnextendScopeWeight() {
		return unextendScopeWeight;
	}
	
	@Override
	public final boolean isVirtual() {
		return true;
	}
}
