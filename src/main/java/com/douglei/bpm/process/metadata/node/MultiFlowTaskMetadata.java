package com.douglei.bpm.process.metadata.node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

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
			flows.sort(flowComparator);
	}
	private static Comparator<FlowMetadata> flowComparator = new Comparator<FlowMetadata>() { // 流排序的比较器
		@Override
		public int compare(FlowMetadata f1, FlowMetadata f2) {
			if(f1.getOrder() == f2.getOrder()) {
				return 0;
			}else if(f1.getOrder() < f2.getOrder()) {
				return -1;
			}
			return 1;
		}
	};

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

	@Override
	public final boolean supportMultiFlow() {
		return true;
	}
}
