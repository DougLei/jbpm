package com.douglei.bpm.process.parser;

import com.douglei.bpm.bean.MultiInstance;

/**
 * 解析器
 * @author DougLei
 */
@MultiInstance
public interface Parser<P, R> {
	
	/**
	 * 获取要解析的元素名称
	 * @return
	 */
	String elementName();
	
	/**
	 * 解析
	 * @param parameter
	 * @return
	 * @throws ProcessParseException
	 */
	R parse(P parameter) throws ProcessParseException;
}
