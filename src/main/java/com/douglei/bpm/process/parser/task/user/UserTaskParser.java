package com.douglei.bpm.process.parser.task.user;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.tmp.data.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class UserTaskParser implements Parser<TaskTemporaryData, UserTaskMetadata> {

	@Override
	public UserTaskMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		// TODO 
		Element element = temporaryData.getElement();
		return new UserTaskMetadata(temporaryData.getId(), element.attributeValue("name"), element.attributeValue("pageID"));
	}

	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}
