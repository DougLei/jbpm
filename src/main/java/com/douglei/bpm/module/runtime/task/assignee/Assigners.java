package com.douglei.bpm.module.runtime.task.assignee;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author DougLei
 */
public class Assigners {
	private List<Assigner> assigners;
	public Assigners(List<Assigner> assigners) {
		this.assigners = assigners;
	}
	
	public boolean isEmpty() {
		return assigners == null || assigners.isEmpty();
	}
	public List<Assigner> getAssigners() {
		return assigners;
	}

	/**
	 * 获取运行指派信息集合
	 * @param taskId
	 * @return
	 */
	public List<Assignee> getAssigneeList(int taskId) {
		List<Assignee> list = new ArrayList<Assignee>(assigners.size());
		assigners.forEach(assigner -> list.add(new Assignee(assigner.getUserId())));
		return list;
	}
}
