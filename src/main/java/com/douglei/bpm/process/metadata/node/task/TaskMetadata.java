package com.douglei.bpm.process.metadata.node.task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.module.runtime.task.entity.Task;
import com.douglei.bpm.process.metadata.ProcessNodeMetadata;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;
import com.douglei.bpm.process.parser.ProcessParseException;

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
		if(!flows.isEmpty()) {
			flows.forEach(f -> {
				if(flow.getMode() != f.getMode())
					throw new ProcessParseException("流程中id=["+id+"]的任务, 引出的多条flow存在不同的mode值["+flow.getMode()+", "+f.getMode()+"]");
			});
		}
		
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
	
	/**
	 * 是否是自动任务节点
	 * @return
	 */
	public boolean isAuto() {
		return false;
	}
	
	/**
	 * 创建流程运行任务实例
	 * @param processDefinitionId
	 * @param processRuntimeInstanceId
	 * @return
	 */
	public Task createProcessRuntimeTask(int processDefinitionId, int processRuntimeInstanceId) {
		Task task = new Task();
		task.setProcdefId(processDefinitionId);
		task.setProcinstId(processRuntimeInstanceId);
		task.setTaskKey(id);
		task.setTaskName(name);
		task.setTaskType(getType().getName());
		task.setStartTime(new Date());
		return task;
	}
}
