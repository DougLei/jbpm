package com.douglei.bpm.process.api.user.option;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class OptionHandler {
	
	/**
	 * 获取option的类型, 必须唯一; 默认值为类名全路径
	 * @return
	 */
	public String getType() {
		return getClass().getName();
	}
	
	/**
	 * 获取xml的结构字符串
	 * @return
	 */
	public final String getXmlStruct() {
		return String.format("<option type=%s>", getType());
	}
	
	/**
	 * 解析Option实例
	 * @param name
	 * @param order
	 * @param userTaskId 所属userTask的id
	 * @param userTaskName 所属userTask的name
	 * @param element
	 * @return
	 * @throws ProcessParseException
	 */
	public abstract Option parse(String name, int order, String userTaskId, String userTaskName, Element element)throws ProcessParseException;
}
