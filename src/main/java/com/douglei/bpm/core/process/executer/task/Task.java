package com.douglei.bpm.core.process.executer.task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.core.process.executer.Node;
import com.douglei.bpm.core.process.executer.flow.Flow;

/**
 * 
 * @author DougLei
 */
public abstract class Task extends Node{
	protected List<Flow> flows;
	
	protected Task(String id, String name) {
		super(id, name);
	}
	
	public void addFlow(Flow flow) {
		if(flows == null) 
			flows = new ArrayList<Flow>(4);
		flows.add(flow);
		if(flows.size() > 1) 
			flows.sort(flowComparator);
	}
	
	// 流排序的比较器
	private static Comparator<Flow> flowComparator = new Comparator<Flow>() {
		@Override
		public int compare(Flow f1, Flow f2) {
			if(f1.getOrder() == f2.getOrder()) {
				return 0;
			}else if(f1.getOrder() < f2.getOrder()) {
				return -1;
			}
			return 1;
		}
	};
}
