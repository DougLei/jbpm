package com.douglei.bpm.process.api.user.option;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class AbstractOptionParser {
	
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
	 * 一个用户任务中, 是否支持配置多个相同type的option; 默认值为false
	 * @return
	 */
	public boolean supportMultiple() {
		return false;
	}
	
	/**
	 * 解析Option实例
	 * @param name
	 * @param order
	 * @param metadata
	 * @param element
	 * @return 
	 * @throws ProcessParseException
	 */
	public abstract Option parse(String name, int order, UserTaskMetadata metadata, Element element)throws ProcessParseException;
}
