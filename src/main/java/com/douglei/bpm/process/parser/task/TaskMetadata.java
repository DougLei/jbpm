package com.douglei.bpm.process.parser.task;

import org.dom4j.Element;

/**
 * 
 * @author DougLei
 */
public class TaskMetadata {
	private String id;
	private Element element;
	
	public TaskMetadata(String id, Element element) {
		this.id = id;
		this.element = element;
	}
	
	/**
	 * 获取xml元素中, 属性id的值
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 获取xml元素
	 * @return
	 */
	public Element getElement() {
		return element;
	}
}
