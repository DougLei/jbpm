package com.douglei.bpm.process.metadata.node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class TaskMetadata extends ProcessNodeMetadata{
	protected List<FlowMetadata> flows = new ArrayList<FlowMetadata>(4);
	
	protected TaskMetadata(String id, String name) {
		super(id, name);
	}
	
	/**
	 * 添加连线
	 * @param flow
	 */
	public final void addFlow(FlowMetadata flow) {
		flows.add(flow);
		if(flows.size() > 1) 
			flows.sort(flowComparator);
	}
	
	// 流排序的比较器
	private static Comparator<FlowMetadata> flowComparator = new Comparator<FlowMetadata>() {
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

	public List<FlowMetadata> getFlows() {
		return flows;
	}
	
	/**
	 * 当前任务是否支持用户介入
	 * @return
	 */
	public boolean supportUserIntervention() {
		return false;
	}
}
