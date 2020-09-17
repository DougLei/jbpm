package com.douglei.bpm.core.process.parser;

/**
 * 解析器
 * @author DougLei
 */
public interface Parser<P, R> {
	
	/**
	 * 解析方法
	 * @param parameter
	 * @return
	 * @throws ProcessParseException
	 */
	R parse(P parameter) throws ProcessParseException;
}
