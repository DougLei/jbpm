package com.douglei.bpm.core.process.parser;

import org.dom4j.Element;

import com.douglei.tools.utils.StringUtil;

/**
 * 解析器
 * @author DougLei
 */
public interface Parser<P, R> {
	
	/**
	 * 获取要解析的元素名称
	 * @return
	 */
	String elementName();
	
	/**
	 * 解析方法
	 * @param parameter
	 * @return
	 * @throws ProcessParseException
	 */
	R parse(P parameter) throws ProcessParseException;
	
	/**
	 * 从xml的Element实例中, 获取id属性值
	 * @param element
	 * @return
	 */
	default String parseId(Element element) {
		String id = element.attributeValue("id");
		if(StringUtil.isEmpty(id))
			throw new ProcessParseException(elementName() + "元素的id值不能为空");
		return id;
	}
}
