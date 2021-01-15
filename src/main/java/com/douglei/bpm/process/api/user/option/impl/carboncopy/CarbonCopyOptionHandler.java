package com.douglei.bpm.process.api.user.option.impl.carboncopy;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyOption;
import com.douglei.bpm.process.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
@Bean(clazz = OptionHandler.class)
public class CarbonCopyOptionHandler extends OptionHandler{
	public static final String TYPE = "carbonCopy";
	
	@Autowired
	private CarbonCopyAssignPolicyParser assignPolicyParser;
	
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public Option parse(String name, int order, String userTaskId, String userTaskName, Element element) throws ProcessParseException {
		element = element.element("candidate");
		if(element == null) 
			throw new ProcessParseException("<userTask id="+userTaskId+" name="+userTaskName+">"+getXmlStruct()+"下必须配置<candidate>");
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(userTaskId, userTaskName, getXmlStruct() + "<candidate>", element);
		return new CarbonCopyOption(TYPE, name, order, new Candidate(assignPolicy, null));
	}
}
