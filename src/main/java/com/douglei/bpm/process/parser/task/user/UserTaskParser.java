package com.douglei.bpm.process.parser.task.user;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.candidate.handle.HandlePolicy;
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
	
	@Override
	public UserTaskMetadata parse(TaskTemporaryData temporaryData) throws ProcessParseException {
		Element element = temporaryData.getElement();
		UserTaskMetadata metadata = new UserTaskMetadata(
				temporaryData.getId(), 
				element.attributeValue("name"), 
				element.attributeValue("defaultFlow"), 
				element.attributeValue("pageID"),
				element.attributeValue("timeLimit"));
		
		parseCandidate(metadata, element.element("candidate"));
		addListener(metadata, element.element("listeners"));
		
		// TODO 有待继续解析
		return metadata;
	}

	// 解析候选人配置
	private void parseCandidate(UserTaskMetadata metadata, Element element) {
		if(element == null)
			throw new NullPointerException("id为["+metadata.getId()+"]的["+getType().getName()+"], 没有配置<candidate>");
		
		AssignPolicy assignPolicy = parseAssignPolicy(metadata, element.element("assignPolicy"));
		HandlePolicy handlePolicy = parseHandlePolicy(metadata, element.element("handlePolicy"));
		metadata.setCandidate(new Candidate(assignPolicy, handlePolicy));
	}
	
	// 解析指派策略
	private AssignPolicy parseAssignPolicy(UserTaskMetadata metadata, Element element) {
		if(element == null)
			throw new NullPointerException("id为["+metadata.getId()+"]的["+getType().getName()+"], 没有配置<assignPolicy>");
		
		
		return null;
	}
	
	// 解析办理策略
	private HandlePolicy parseHandlePolicy(UserTaskMetadata metadata, Element element) {
		if(element == null)
			throw new NullPointerException("id为["+metadata.getId()+"]的["+getType().getName()+"], 没有配置<handlePolicy>");
		
		
		
		return null;
	}

	@Override
	public Type getType() {
		return Type.USER_TASK;
	}
}
