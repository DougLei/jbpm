package com.douglei.bpm.process.executer.task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.douglei.bpm.process.executer.ProcessNode;
import com.douglei.bpm.process.executer.flow.Flow;
import com.douglei.bpm.process.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
public abstract class Task extends ProcessNode{
	protected List<Flow> flows = new ArrayList<Flow>(5);
	
	protected Task(String id, String name) {
		super(id, name);
	}
	
	/**
	 * 添加连线
	 * @param flow
	 */
	public void addFlow(Flow flow) {
		if(!flows.isEmpty()) {
			flows.forEach(f -> {
				if(flow.getMode() != f.getMode())
					throw new ProcessParseException("在id为["+id+"], name为["+name+"]的["+getClass().getSimpleName()+"]中, 引出的多条flow存在不同的mode值["+flow.getMode()+", "+f.getMode()+"]");
			});
		}
		
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
