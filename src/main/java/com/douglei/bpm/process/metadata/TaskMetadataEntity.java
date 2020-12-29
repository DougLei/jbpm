package com.douglei.bpm.process.metadata;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * 
 */
public class TaskMetadataEntity<M extends TaskMetadata> {
	private M taskMetadata;
	private List<FlowMetadata> flows;

	TaskMetadataEntity(M taskMetadata, List<FlowMetadata> flows) {
		this.taskMetadata = taskMetadata;
		this.flows = flows;
	}
	
	public M getTaskMetadata() {
		return taskMetadata;
	}
	public List<FlowMetadata> getInputFlows() {
		List<FlowMetadata> inputFlows = new ArrayList<FlowMetadata>(4);
		flows.forEach(flow -> {
			if(flow.getTarget().equals(taskMetadata.getId()))
				inputFlows.add(flow);
		});
		return inputFlows;
	}
	public List<FlowMetadata> getOutputFlows() {
		List<FlowMetadata> outputFlows = new ArrayList<FlowMetadata>(4);
		flows.forEach(flow -> {
			if(flow.getSource().equals(taskMetadata.getId()))
				outputFlows.add(flow);
		});
		if(outputFlows.size() > 1)
			outputFlows.sort(flowSortComparator);
		return outputFlows;
	}
	public FlowMetadata getDefaultOutputFlow() {
		if(taskMetadata.getDefaultOutputFlowId() == null)
			return null;
		
		for (FlowMetadata flow : flows) {
			if(flow.getId().equals(taskMetadata.getDefaultOutputFlowId())) 
				return flow;
		}
		return null;
	}
	
	// flow的排序比较器
	private static final Comparator<FlowMetadata> flowSortComparator = new Comparator<FlowMetadata>() {
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
}
