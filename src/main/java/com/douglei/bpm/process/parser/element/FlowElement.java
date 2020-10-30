package com.douglei.bpm.process.parser.element;

import org.dom4j.Element;

import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class FlowElement extends TaskElement{
	private String source;
	private String target;
	
	public FlowElement(String id, Element element) {
		super(id, element);
		
		this.source = element.attributeValue("source");
		if(StringUtil.isEmpty(this.source))
			throw new ProcessParseException("工作流中, flow元素的source不能为空");
		
		this.target = element.attributeValue("target");
		if(StringUtil.isEmpty(this.target))
			throw new ProcessParseException("工作流中, flow元素的target不能为空");
	}

	public String getSource() {
		return source;
	}
	public String getTarget() {
		return target;
	}
}
