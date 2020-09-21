package com.douglei.bpm.core.process.parser.impl.task;

import com.douglei.bpm.bean.annotation.ParserBean;
import com.douglei.bpm.core.process.executer.task.impl.SqlTask;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.parser.element.TaskElement;

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
