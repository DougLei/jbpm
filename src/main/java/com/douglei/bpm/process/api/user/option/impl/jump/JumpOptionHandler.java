package com.douglei.bpm.process.api.user.option.impl.jump;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.jump.JumpOption;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.task.user.AssignPolicyParser;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
@Bean(clazz = OptionHandler.class)
public class JumpOptionHandler extends OptionHandler{
	public static final String TYPE = "jump";
	
	@Autowired
	private AssignPolicyParser assignPolicyParser;
	
	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public Option parse(String name, int order, String userTaskId, String userTaskName, Element element) throws ProcessParseException {
		String target = null;
		boolean suggest = false;
		boolean attitude = false;
		boolean reason = false;
		
		Element parameterElement = element.element("parameter");
		if(parameterElement != null) {
			target = parameterElement.attributeValue("target");
			if(StringUtil.isEmpty(target))
				target = null;
			
			suggest = "true".equalsIgnoreCase(parameterElement.attributeValue("suggest"));
			attitude = "true".equalsIgnoreCase(parameterElement.attributeValue("attitude"));
			reason = "true".equalsIgnoreCase(parameterElement.attributeValue("reason"));
		}
		
		element = element.element("candidate");
		if(element == null) 
			return new JumpOption(TYPE, name, order, reason, target, suggest, attitude, null);
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(userTaskId, userTaskName, getXmlStruct(), element);
		return new JumpOption(TYPE, name, order, reason, target, suggest, attitude, new Candidate(assignPolicy, null));
	}
}
