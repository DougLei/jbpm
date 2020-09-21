package com.douglei.bpm.core.process.parser.impl.task;

import com.douglei.bpm.bean.annotation.ParserBean;
import com.douglei.bpm.core.process.executer.task.impl.usertask.UserTask;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.parser.element.TaskElement;

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
