package com.douglei.bpm.core.process.parser.impl.task.gateway;

import com.douglei.bpm.bean.annotation.ParserBean;
import com.douglei.bpm.core.process.executer.flow.Flow;
import com.douglei.bpm.core.process.parser.Parser;
import com.douglei.bpm.core.process.parser.ProcessParseException;
import com.douglei.bpm.core.process.parser.element.TaskElement;

/**
 * 
 * @author DougLei
 */
@ParserBean
public class ParallelGatewayParser implements Parser<TaskElement, Flow> {

	@Override
	public String elementName() {
		return "parallelGateway";
	}

	@Override
	public Flow parse(TaskElement taskElement) throws ProcessParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
