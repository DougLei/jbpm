package com.douglei.bpm.process.parser;

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
}
