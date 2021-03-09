package com.douglei.bpm.process.mapping.parser;

import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.ProcessNodeMetadata;
import com.douglei.bpm.process.mapping.parser.temporary.data.TemporaryData;

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
