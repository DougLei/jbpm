package com.douglei.bpm.process.handler.components.assignee;

import java.util.List;

/**
 * 指派人的集合(包装类)
 * @author DougLei
 */
public class Assigners {
	private List<Assigner> list;
	public Assigners(List<Assigner> list) {
		this.list = list;
	}
	
	public int size() {
		return list.size();
	}
	public boolean isEmpty() {
		return list == null || list.isEmpty();
	}
	public List<Assigner> getList() {
		return list;
	}
}
