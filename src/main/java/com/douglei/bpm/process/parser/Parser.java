package com.douglei.bpm.process.parser;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.process.NodeType;

/**
 * 解析器
 * @author DougLei
 */
@MultiInstance
public interface Parser<P, R> {
	
	/**
	 * 
	 * @return
	 */
	NodeType getNodeType();
	
	/**
	 * 解析
	 * @param parameter
	 * @return
	 * @throws ProcessParseException
	 */
	R parse(P parameter) throws ProcessParseException;
}
