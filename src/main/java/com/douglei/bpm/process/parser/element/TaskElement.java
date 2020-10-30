package com.douglei.bpm.process.parser.element;

import org.dom4j.Element;

/**
 * 
 * @author DougLei
 */
public class TaskElement {
	private String id;
	private Element element;
	
	public TaskElement(String id, Element element) {
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
	 * 获取xml元素中, 属性name的值
	 * @return
	 */
	public String getName() {
		return element.attributeValue("name");
	}
	
	/**
	 * 获取xml元素
	 * @return
	 */
	public Element getElement() {
		return element;
	}
}
