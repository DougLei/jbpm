package com.douglei.bpm.process.parser.task.user;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.parser.GeneralParser;
import com.douglei.bpm.process.parser.Parser;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.tmp.data.TaskTemporaryData;

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
	
	@Override
	public UserTaskMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		
		String id = temporaryData.getId();
		String name = element.attributeValue("name");
		UserTaskMetadata metadata = new UserTaskMetadata(
				id, name, 
				element.attributeValue("defaultOutputFlow"), 
				element.attributeValue("pageID"),
				timeLimitParser.parse(id, name, element.attributeValue("timeLimit")),
				candidateParser.parse(id, name, element.element("candidate")));
		
		// TODO 后续解析<option>
		
		addListener(metadata, element.element("listeners"));
		return metadata;
	}
	
	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}
