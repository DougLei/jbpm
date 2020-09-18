package com.douglei.bpm.core.process.parser;

import org.dom4j.Element;

import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class FlowElement {
	private String source;
	private String target;
	private Element element;
	
	public FlowElement(Element element) {
		this.source = element.attributeValue("source");
		if(StringUtil.isEmpty(this.source))
			throw new ProcessParseException("flow元素的source不能为空");
		
		this.target = element.attributeValue("target");
		if(StringUtil.isEmpty(this.target))
			throw new ProcessParseException("flow元素的target不能为空");
		
		this.element = element;
	}

	public String getSource() {
		return source;
	}
	public String getTarget() {
		return target;
	}
	public Element getElement() {
		return element;
	}
}
