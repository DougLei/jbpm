package com.douglei.bpm.process.parser.task.user;

import com.douglei.bpm.process.executer.task.user.UserTask;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class UserTaskParser implements Parser<TaskMetadata, UserTask> {

	@Override
	public String elementName() {
		return "userTask";
	}

	@Override
	public UserTask parse(TaskMetadata taskMetadata) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
