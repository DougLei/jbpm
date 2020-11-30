package com.douglei.bpm.process.parser.task.event;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.event.EndEventMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

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
	public NodeType getNodeType() {
		return NodeType.END_EVENT;
	}
}
