package com.douglei.bpm.module.runtime.task.assignee;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class Assigners {
	private List<Assigner> assigners;
	public Assigners() {}
	public Assigners(List<Assigner> assigners) {
		this.assigners = assigners;
	}
	
	public boolean isEmpty() {
		return assigners == null || assigners.isEmpty();
	}
	public List<Assigner> getAssigners() {
		return assigners;
	}
	public void addAssigner(Assigner assigner) {
		if(assigners == null)
			assigners = new ArrayList<Assigner>();
		else if(assigners.contains(assigner))
			return;
		assigners.add(assigner);
	}

	/**
	 * 获取运行指派信息集合
	 * @param taskId
	 * @return
	 */
	public List<Assignee> getAssigneeList(int taskId) {
		List<Assignee> list = new ArrayList<Assignee>(assigners.size());
		assigners.forEach(assigner -> list.add(new Assignee(taskId, assigner.getUserId())));
		
		// TODO 提前委托的指派人, 就要在这里处理
		
		return list;
	}
}
