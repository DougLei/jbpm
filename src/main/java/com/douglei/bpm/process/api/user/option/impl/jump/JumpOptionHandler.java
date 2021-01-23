package com.douglei.bpm.process.api.user.option.impl.jump;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
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
public abstract class JumpOptionHandler extends OptionHandler{
	
	@Autowired
	private AssignPolicyParser assignPolicyParser;
	
	@Override
	public Option parse(String name, int order, String userTaskId, String userTaskName, Element element) throws ProcessParseException {
		String target = null;
		boolean suggestIsRequired = false;
		boolean attitudeIsRequired = false;
		boolean reasonIsRequired = false;
		
		Element parameterElement = element.element("parameter");
		if(parameterElement != null) {
			target = parameterElement.attributeValue("target");
			if(StringUtil.isEmpty(target))
				target = null;
			
			suggestIsRequired = "true".equalsIgnoreCase(parameterElement.attributeValue("suggest"));
			attitudeIsRequired = "true".equalsIgnoreCase(parameterElement.attributeValue("attitude"));
			reasonIsRequired = "true".equalsIgnoreCase(parameterElement.attributeValue("reason"));
		}
		
		element = element.element("candidate");
		if(element == null) 
			return new JumpOption(getType(), name, order, reasonIsRequired, target, suggestIsRequired, attitudeIsRequired, null);
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(userTaskId, userTaskName, getXmlStruct(), element);
		return new JumpOption(getType(), name, order, reasonIsRequired, target, suggestIsRequired, attitudeIsRequired, new Candidate(assignPolicy, null));
	}
}
