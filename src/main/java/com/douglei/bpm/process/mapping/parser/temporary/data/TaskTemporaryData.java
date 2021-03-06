package com.douglei.bpm.process.mapping.parser.temporary.data;

import org.dom4j.Element;

/**
 * 
 * @author DougLei
 */
public class TaskTemporaryData implements TemporaryData{
	private String id;
	private Element element;
	
	public TaskTemporaryData(String id, Element element) {
		this.id = id;
		this.element = element;
	}
	
	/**
	 * 获取xml元素中, 属性id的值
	 * @return
	 */
	public final String getId() {
		return id;
	}
	
	/**
	 * 获取xml元素
	 * @return
	 */
	public final Element getElement() {
		return element;
	}
}
