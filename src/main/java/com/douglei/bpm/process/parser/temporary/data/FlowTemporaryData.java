package com.douglei.bpm.process.parser.temporary.data;

import org.dom4j.Element;

import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class FlowTemporaryData extends TaskTemporaryData{
	private String source;
	private String target;
	
	public FlowTemporaryData(String id, Element element) {
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
