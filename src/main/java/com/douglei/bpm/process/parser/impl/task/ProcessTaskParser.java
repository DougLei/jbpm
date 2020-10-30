package com.douglei.bpm.process.parser.impl.task;

import com.douglei.bpm.bean.annotation.ParserBean;
import com.douglei.bpm.process.executer.task.impl.ProcessTask;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class ProcessTaskParser implements Parser<TaskElement, ProcessTask> {

	@Override
	public String elementName() {
		return "processTask";
	}

	@Override
	public ProcessTask parse(TaskElement taskElement) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
