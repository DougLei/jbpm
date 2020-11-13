package com.douglei.bpm.process.parser.flow;

import org.dom4j.Element;

import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskMetadata;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public class FlowMetadata extends TaskMetadata{
	private String source;
	private String target;
	
	public FlowMetadata(String id, Element element) {
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
