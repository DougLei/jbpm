package com.douglei.bpm.process.parser.impl.task;

import com.douglei.bpm.process.executer.task.impl.SqlTask;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ParserBean;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class SqlTaskParser implements Parser<TaskElement, SqlTask> {

	@Override
	public String elementName() {
		return "sqlTask";
	}

	@Override
	public SqlTask parse(TaskElement taskElement) throws ProcessParseException {
		return null;
	}
}
