package com.douglei.bpm.process.api.user.option.impl.carboncopy;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.metadata.task.user.option.carboncopy.CarbonCopyOption;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
@Bean(clazz = OptionHandler.class)
public class CarbonCopyOptionHandler extends OptionHandler{
	
	@Autowired
	private CarbonCopyAssignPolicyParser assignPolicyParser;
	
	@Override
	public String getType() {
		return OptionTypeConstants.CARBON_COPY;
	}

	@Override
	public Option parse(String name, int order, UserTaskMetadata metadata, Element element) throws ProcessParseException {
		element = element.element("candidate");
		if(element == null) 
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">"+getXmlStruct()+"下必须配置<candidate>");
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(metadata, getXmlStruct() + "<candidate>", element);
		return new CarbonCopyOption(OptionTypeConstants.CARBON_COPY, name, order, new Candidate(assignPolicy, null));
	}
}
