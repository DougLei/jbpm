package com.douglei.bpm.process.metadata;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * 
 */
public class TaskMetadataEntity<M extends TaskMetadata> {
	private M taskMetadata;
	private List<FlowMetadata> inputFlows;
	private List<FlowMetadata> outputFlows;
	private FlowMetadata defaultFlow;

	TaskMetadataEntity(M taskMetadata, List<FlowMetadata> flows) {
		this.taskMetadata = taskMetadata;
		
		for (FlowMetadata flow : flows) {
			if(taskMetadata.requiredInputFlows() && flow.getTarget().equals(taskMetadata.getId())) {
				if(this.inputFlows == null)
					this.inputFlows = new ArrayList<FlowMetadata>(4);
				this.inputFlows.add(flow);
			}
			if(taskMetadata.requiredOutputFlows() && flow.getSource().equals(taskMetadata.getId())) {
				if(this.outputFlows == null)
					this.outputFlows = new ArrayList<FlowMetadata>(4);
				this.outputFlows.add(flow);
			}
			if(taskMetadata.requiredDefaultOutputFlow() && taskMetadata.getDefaultOutputFlowId() != null && this.defaultFlow==null && flow.getId().equals(taskMetadata.getDefaultOutputFlowId())) {
				this.defaultFlow = flow;
			}
		}
	}
	
	public M getTaskMetadata() {
		return taskMetadata;
	}
	public List<FlowMetadata> getInputFlows() {
		return inputFlows;
	}
	public List<FlowMetadata> getOutputFlows() {
		return outputFlows;
	}
	public FlowMetadata getDefaultFlow() {
		return defaultFlow;
	}
}
