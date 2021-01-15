package com.douglei.bpm.process.parser.event;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.event.EndEventMetadata;
import com.douglei.bpm.process.parser.GeneralParser;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.temporary.data.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class EndEventParser extends GeneralParser implements Parser<TaskTemporaryData, EndEventMetadata> {

	@Override
	public EndEventMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		EndEventMetadata metadata = new EndEventMetadata(temporaryData.getId(), element.attributeValue("name"));
		
		addListener(metadata, element.element("listeners"));
		return metadata;
	}

	@Override
	public Type getType() {
		return Type.END_EVENT;
	}
}
