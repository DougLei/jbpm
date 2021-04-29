package com.douglei.bpm.process.mapping.parser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.process.api.APIContainer;
import com.douglei.bpm.process.api.listener.ListenParser;
import com.douglei.bpm.process.mapping.metadata.ProcessNodeMetadata;
import com.douglei.bpm.process.mapping.metadata.listener.ActiveTime;
import com.douglei.bpm.process.mapping.metadata.listener.Listener;

/**
 * 
 * @author DougLei
 */
public abstract class GeneralParser {
	
	@Autowired
	private APIContainer apiContainer;
	
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
	protected final void addListener(ProcessNodeMetadata metadata, Element element) {
		if(element == null)
			return;
		
		List<Element> elements = element.elements("listener");
		if(elements.isEmpty())
			return;
		
		List<Listener> listeners = new ArrayList<Listener>(elements.size());
		elements.forEach(elem -> {
			ListenParser listenParser = apiContainer.getListenParser(elem.attributeValue("class"));
			if(listenParser == null)
				throw new ProcessParseException(String.format("<%s id=%s name=%s><listeners><listener>的class属性值[%s]不合法", metadata.getType().getName(), metadata.getId(), metadata.getName(), elem.attributeValue("class")));
			
			ActiveTime activeTime = ActiveTime.valueOf(elem.attributeValue("activeTime").toUpperCase());
			listeners.add(listenParser.parse(activeTime, element));
		});
		metadata.setListeners(listeners);
	}
}
