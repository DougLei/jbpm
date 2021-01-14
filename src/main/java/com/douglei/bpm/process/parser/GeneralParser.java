package com.douglei.bpm.process.parser;

import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.process.metadata.ProcessNodeMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class GeneralParser {
	
	/**
	 * 解析条件表达式
	 * @param element
	 * @return
	 */
	protected final String parseConditionExpression(Element element) {
		if(element == null)
			return null;
		
		String text = element.getTextTrim();
		if(text.length() == 0)
			return null;
		return text;
	}
	
	/**
	 * 添加配置的监听器
	 * @param taskMetadata
	 * @param listeners
	 */
	@SuppressWarnings("unchecked")
	protected final void addListener(ProcessNodeMetadata metadata, Element listeners) {
		if(listeners == null)
			return;
		
		List<Element> elements = listeners.elements("listener");
		if(elements.isEmpty())
			return;
		
		// TODO 添加配置的监听器
	}
}
