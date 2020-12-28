package com.douglei.bpm.process.metadata;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class MultiFlowTaskMetadata extends TaskMetadata{
	private String defaultFlowId;
	private List<FlowMetadata> flows = new ArrayList<FlowMetadata>(4);
	
	protected MultiFlowTaskMetadata(String id, String name, String defaultFlowId) {
		super(id, name);
		this.defaultFlowId = defaultFlowId;
	}

	@Override
	public final void addFlow(FlowMetadata flow) {
		flows.add(flow);
		if(flows.size() > 1) 
			flows.sort(FlowSortComparator.getInstance());
	}

	@Override
	public final FlowMetadata getDefaultFlow() {
		if(defaultFlowId == null)
			return null;
		for (FlowMetadata flowMetadata : flows) {
			if(flowMetadata.getId().equals(defaultFlowId))
				return flowMetadata;
		}
		return null;
	}

	@Override
	public final List<FlowMetadata> getFlows() {
		return flows;
	}
}
