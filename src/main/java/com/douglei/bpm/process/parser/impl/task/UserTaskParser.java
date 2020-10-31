package com.douglei.bpm.process.parser.impl.task;

import com.douglei.bpm.process.executer.task.impl.usertask.UserTask;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class UserTaskParser implements Parser<TaskElement, UserTask> {

	@Override
	public String elementName() {
		return "userTask";
	}

	@Override
	public UserTask parse(TaskElement taskElement) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
