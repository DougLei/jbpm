package com.douglei.bpm.process.parser.event;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.event.StartEventMetadata;
import com.douglei.bpm.process.parser.GeneralParser;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.tmp.data.TaskTemporaryData;

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
