package com.douglei.bpm.process.api.user.option.impl.jump;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.metadata.task.user.option.jump.JumpOption;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.bpm.process.mapping.parser.task.user.AssignPolicyParser;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public abstract class JumpOptionHandler extends OptionHandler{
	
	@Autowired
	private AssignPolicyParser assignPolicyParser;
	
	@Override
	public Option parse(String name, int order, UserTaskMetadata metadata, Element element) throws ProcessParseException {
		String target = null;
		boolean suggestIsRequired = false;
		boolean attitudeIsRequired = false;
		boolean reasonIsRequired = false;
		
		Element parameterElement = element.element("parameter");
		if(parameterElement != null) {
			target = parameterElement.attributeValue("target");
			if(StringUtil.isEmpty(target))
				throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">"+getXmlStruct()+"<parameter>的target属性值不能为空");
			
			// 解析suggest
			String value = parameterElement.attributeValue("suggest");
			suggestIsRequired = StringUtil.isEmpty(value)?metadata.getCandidate().getHandlePolicy().suggestIsRequired():"true".equalsIgnoreCase(value);
			
			// 解析attitude
			value = parameterElement.attributeValue("attitude");
			attitudeIsRequired = StringUtil.isEmpty(value)?metadata.getCandidate().getHandlePolicy().attitudeIsRequired():"true".equalsIgnoreCase(value);
			
			// 解析reason
			reasonIsRequired = "true".equalsIgnoreCase(parameterElement.attributeValue("reason"));
		}
		
		element = element.element("candidate");
		if(element == null) 
			return new JumpOption(getType(), name, order, reasonIsRequired, target, suggestIsRequired, attitudeIsRequired, null);
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(metadata, getXmlStruct(), element);
		return new JumpOption(getType(), name, order, reasonIsRequired, target, suggestIsRequired, attitudeIsRequired, new Candidate(assignPolicy, null));
	}
}
