package com.douglei.bpm.process.parser;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;
import com.douglei.bpm.process.parser.tmp.data.TemporaryData;

/**
 * 解析器
 * @author DougLei
 */
@MultiInstance
public interface Parser<T extends TemporaryData, M extends ProcessNodeMetadata> {
	
	/**
	 * 
	 * @return
	 */
	Type getType();
	
	/**
	 * 解析
	 * @param temporaryData
	 * @return
	 * @throws ProcessParseException
	 */
	M parse(T temporaryData) throws ProcessParseException;
}
