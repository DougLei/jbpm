package com.douglei.bpm.process.mapping.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.process.mapping.metadata.flow.FlowMetadata;

/**
 * 任务元数据实体类
 * @author DougLei
 */
public class TaskMetadataEntity<M extends TaskMetadata> {
	private ProcessMetadata processMetadata;
	private M taskMetadata;
	private List<FlowMetadata> flows;
	
	private List<FlowMetadata> inputFlows;
	private List<FlowMetadata> outputFlows;
	
	TaskMetadataEntity(ProcessMetadata processMetadata, M taskMetadata, List<FlowMetadata> flows) {
		this.processMetadata = processMetadata;
		this.taskMetadata = taskMetadata;
		this.flows = flows;
	}
	
	/**
	 * 获取流程元数据实例
	 * @return
	 */
	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	
	/**
	 * 获取任务元数据实例
	 * @return
	 */
	public M getTaskMetadata() {
		return taskMetadata;
	}
	
	/**
	 * 获取任务的输入流集合
	 * @return
	 */
	public List<FlowMetadata> getInputFlows() {
		if(!taskMetadata.supportInputFlows()) 
			return Collections.emptyList();
		
		if(inputFlows == null) {
			inputFlows = new ArrayList<FlowMetadata>(4);
			flows.forEach(flow -> {
				if(flow.getTarget().equals(taskMetadata.getId()))
					inputFlows.add(flow);
			});
		}
		return inputFlows;
	}
	
	/**
	 * 获取任务的输出流集合
	 * @return
	 */
	public List<FlowMetadata> getOutputFlows() {
		if(!taskMetadata.supportOutFlows())
			return Collections.emptyList();
		
		if(outputFlows == null) {
			outputFlows = new ArrayList<FlowMetadata>(4);
			flows.forEach(flow -> {
				if(flow.getSource().equals(taskMetadata.getId()))
					outputFlows.add(flow);
			});
			if(outputFlows.size() > 1)
				outputFlows.sort(FLOW_SORT_COMPARATOR);
		}
		return outputFlows;
	}
	
	/**
	 * 获取任务默认输出流
	 * @return
	 */
	public FlowMetadata getDefaultOutputFlow() {
		if(taskMetadata.getDefaultOutputFlowId() == null)
			return null;
		
		for (FlowMetadata flow : getOutputFlows()) {
			if(flow.getId().equals(taskMetadata.getDefaultOutputFlowId()))
				return flow;
		}
		return null;
	}
	
	// flow的排序比较器
	private static final Comparator<FlowMetadata> FLOW_SORT_COMPARATOR = new Comparator<FlowMetadata>() {
		@Override
		public int compare(FlowMetadata f1, FlowMetadata f2) {
			if(f1.getOrder() == f2.getOrder()) 
				return 0;
			if(f1.getOrder() < f2.getOrder()) 
				return -1;
			return 1;
		}
	};
}
