package com.douglei.bpm.process.metadata.node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class SingleFlowTaskMetadata extends TaskMetadata {
	private FlowMetadata flow;
	
	protected SingleFlowTaskMetadata(String id, String name) {
		super(id, name);
	}
	
	@Override
	public final boolean supportMultiFlow() {
		return false;
	}
	
	@Override
	public final void addFlow(FlowMetadata flow) {
		this.flow = flow;
	}
	
	@Override
	public final List<FlowMetadata> getFlows() {
		if(flow == null)
			return Collections.emptyList();
		return Arrays.asList(flow);
	}
	
	@Override
	public final FlowMetadata getDefaultFlow() {
		return null;
	}

	@Override
	public final boolean supportFlowConditionExpr() {
		return false;
	}
}
