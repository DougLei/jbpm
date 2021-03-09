package com.douglei.bpm.process.mapping.parser.task.user;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.parser.GeneralParser;
import com.douglei.bpm.process.mapping.parser.Parser;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.bpm.process.mapping.parser.temporary.data.TaskTemporaryData;

/**
 * 
 * @author DougLei
 */
@Bean(clazz=Parser.class)
public class UserTaskParser extends GeneralParser implements Parser<TaskTemporaryData, UserTaskMetadata> {
	
	@Autowired
	private TimeLimitParser timeLimitParser;
	
	@Autowired
	private CandidateParser candidateParser;
	
	@Autowired
	private OptionParser optionParser;
	
	@Override
	@SuppressWarnings("unchecked")
	public UserTaskMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		
		String id = temporaryData.getId();
		String name = element.attributeValue("name");
		UserTaskMetadata metadata = new UserTaskMetadata(
				id, name, 
				element.attributeValue("defaultOutputFlow"), 
				element.attributeValue("pageID"),
				timeLimitParser.parse(id, name, element.attributeValue("timeLimit")),
				candidateParser.parse(id, name, element.element("candidate")),
				optionParser.parse(id, name, element.elements("option")));
		
		addListener(metadata, element.element("listeners"));
		return metadata;
	}

	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}
