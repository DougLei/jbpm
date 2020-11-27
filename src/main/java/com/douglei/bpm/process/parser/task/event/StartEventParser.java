package com.douglei.bpm.process.parser.task.event;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.event.StartEventMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class StartEventParser implements Parser<TaskTemporaryData, StartEventMetadata> {
	
	@Override
	public StartEventMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		return new StartEventMetadata(temporaryData.getId(), element.attributeValue("name"), getNodeType(), element.attributeValue("conditionExpr"));
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.START_EVENT;
	}
}
