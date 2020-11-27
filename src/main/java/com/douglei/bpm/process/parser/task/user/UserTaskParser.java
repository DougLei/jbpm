package com.douglei.bpm.process.parser.task.user;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.task.user.UserTaskMetadata;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class UserTaskParser implements Parser<TaskTemporaryData, UserTaskMetadata> {

	@Override
	public UserTaskMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.USER_TASK;
	}
}
