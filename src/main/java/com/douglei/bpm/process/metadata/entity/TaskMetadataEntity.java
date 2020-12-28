package com.douglei.bpm.process.metadata.entity;

import java.util.ArrayList;
import java.util.List;

import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * 
 */
public class TaskMetadataEntity {
	private TaskMetadata taskMetadata;
	private List<FlowMetadata> inputFlows;
	private List<FlowMetadata> outputFlows;
	private FlowMetadata defaultFlow;

	public TaskMetadataEntity(TaskMetadata taskMetadata, List<FlowMetadata> flows, boolean inputFlows, boolean outputFlows, boolean defaultFlow) {
		this.taskMetadata = taskMetadata;
		
		for (FlowMetadata flow : flows) {
			if(inputFlows && flow.getTarget().equals(taskMetadata.getId())) {
				if(this.inputFlows == null)
					this.inputFlows = new ArrayList<FlowMetadata>(4);
				this.inputFlows.add(flow);
			}
			if(outputFlows && flow.getSource().equals(taskMetadata.getId())) {
				if(this.outputFlows == null)
					this.outputFlows = new ArrayList<FlowMetadata>(4);
				this.outputFlows.add(flow);
			}
			if(defaultFlow && this.defaultFlow == null) {
				if(flow.getId().equals(taskMetadata.getDefaultFlowId()))
					this.defaultFlow = flow;
			}
		}
	}
	
	public TaskMetadata getTaskMetadata() {
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
