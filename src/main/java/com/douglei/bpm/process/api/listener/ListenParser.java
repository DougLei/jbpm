package com.douglei.bpm.process.api.listener;

import org.dom4j.Element;

import com.douglei.bpm.process.mapping.metadata.listener.ActiveTime;
import com.douglei.bpm.process.mapping.metadata.listener.Listener;

/**
 * 
 * @author DougLei
 */
public interface ListenParser {
	
	/**
	 * 解析Listener实例
	 * @param activeTime
	 * @param element
	 * @return
	 */
	Listener parse(ActiveTime activeTime, Element element);
}
