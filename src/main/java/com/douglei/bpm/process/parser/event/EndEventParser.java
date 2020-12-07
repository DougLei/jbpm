package com.douglei.bpm.process.parser.event;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.event.EndEventMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.tmp.data.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class EndEventParser implements Parser<TaskTemporaryData, EndEventMetadata> {

	@Override
	public EndEventMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		return new EndEventMetadata(temporaryData.getId(), temporaryData.getElement().attributeValue("name"));
	}

	@Override
	public Type getType() {
		return Type.END_EVENT;
	}
}
