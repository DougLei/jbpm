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
	private FlowMetadataList outputFlows;
	private FlowMetadata defaultOutputFlow;
	private FlowMetadataList inputFlows;

	TaskMetadataEntity(M taskMetadata, List<FlowMetadata> flows) {
		this.taskMetadata = taskMetadata;
		
		for (FlowMetadata flow : flows) {
			if(taskMetadata.requiredOutputFlows() && flow.getSource().equals(taskMetadata.getId())) {
				if(this.outputFlows == null)
					this.outputFlows = new FlowMetadataList();
				this.outputFlows.addFlow(flow);
			}
			if(taskMetadata.getDefaultOutputFlowId() != null && flow.getId().equals(taskMetadata.getDefaultOutputFlowId())) {
				this.defaultOutputFlow = flow;
			}
			if(taskMetadata.requiredInputFlows() && flow.getTarget().equals(taskMetadata.getId())) {
				if(this.inputFlows == null)
					this.inputFlows = new FlowMetadataList();
				this.inputFlows.addFlow(flow);
			}
		}
	}
	
	public M getTaskMetadata() {
		return taskMetadata;
	}
	public List<FlowMetadata> getOutputFlows() {
		if(outputFlows == null)
			return null;
		return outputFlows.getFlows();
	}
	public FlowMetadata getDefaultOutputFlow() {
		return defaultOutputFlow;
	}
	public List<FlowMetadata> getInputFlows() {
		if(inputFlows == null)
			return null;
		return inputFlows.getFlows();
	}
	
	// 流集合实例
	private class FlowMetadataList {
		private boolean sorted;
		private List<FlowMetadata> flows;
		
		public void addFlow(FlowMetadata flow) {
			if(flows == null)
				flows = new ArrayList<FlowMetadata>(4);
			flows.add(flow);
		}
		public List<FlowMetadata> getFlows() {
			if(!sorted && flows.size() > 1) {
				flows.sort(flowSortComparator);
				sorted = true;
			}
			return flows;
		}
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
