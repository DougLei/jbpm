package com.douglei.bpm.process.parser.task.user;

import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.process.node.task.user.UserTask;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class, supportMultiInstance=true)
public class UserTaskParser implements Parser<TaskTemporaryData, UserTask> {

	@Override
	public String elementName() {
		return "userTask";
	}

	@Override
	public UserTask parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
