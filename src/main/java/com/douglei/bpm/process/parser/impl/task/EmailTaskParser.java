package com.douglei.bpm.process.parser.impl.task;

import com.douglei.bpm.bean.annotation.ParserBean;
import com.douglei.bpm.process.executer.task.impl.EmailTask;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class EmailTaskParser implements Parser<TaskElement, EmailTask> {

	@Override
	public String elementName() {
		return "emailTask";
	}

	@Override
	public EmailTask parse(TaskElement taskElement) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
