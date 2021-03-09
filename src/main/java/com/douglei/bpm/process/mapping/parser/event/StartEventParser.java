package com.douglei.bpm.process.mapping.parser.event;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.event.StartEventMetadata;
import com.douglei.bpm.process.mapping.parser.GeneralParser;
import com.douglei.bpm.process.mapping.parser.Parser;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.bpm.process.mapping.parser.temporary.data.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class StartEventParser extends GeneralParser implements Parser<TaskTemporaryData, StartEventMetadata> {
	
	@Override
	public StartEventMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		StartEventMetadata metadata = new StartEventMetadata(
				temporaryData.getId(), 
				element.attributeValue("name"), 
				element.attributeValue("defaultOutputFlow"), 
				parseConditionExpression(element.element("conditionExpression")));
		
		addListener(metadata, element.element("listeners"));
		return metadata;
	}

	@Override
	public Type getType() {
		return Type.START_EVENT;
	}
}
