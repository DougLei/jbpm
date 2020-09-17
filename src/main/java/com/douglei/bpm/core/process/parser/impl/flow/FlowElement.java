package com.douglei.bpm.core.process.parser.impl.flow;

import org.dom4j.Element;

/**
 * 
 * @author DougLei
 */
public class FlowElement {
	private String source;
	private Element element;
	
	public FlowElement(String source, Element element) {
		this.source = source;
		this.element = element;
	}

	public String getSource() {
		return source;
	}
	public Element getElement() {
		return element;
	}
}
