package com.douglei.bpm.process.parser.task.event;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.executer.task.event.StartEvent;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.TaskMetadata;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction = false)
public class StartEventParser implements Parser<TaskMetadata, StartEvent> {
	
	@Override
	public String elementName() {
		return "startEvent";
	}

	@Override
	public StartEvent parse(TaskMetadata taskMetadata) throws ProcessParseException {
		Element element = taskMetadata.getElement();
		return new StartEvent(taskMetadata.getId(), element.attributeValue("name"), element.attributeValue("conditionExpr"));
	}
}
