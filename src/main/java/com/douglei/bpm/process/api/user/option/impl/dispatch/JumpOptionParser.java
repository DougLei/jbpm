package com.douglei.bpm.process.api.user.option.impl.dispatch;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.metadata.task.user.option.dispatch.JumpOption;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.bpm.process.mapping.parser.task.user.AssignPolicyParser;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public class JumpOptionParser extends AbstractDispatchOptionParser{
	
	@Autowired
	private AssignPolicyParser assignPolicyParser;

	@Override
	public String getType() {
		return OptionTypeConstants.JUMP;
	}
	
	@Override
	protected Option parse(String name, int order, boolean suggestIsRequired, boolean attitudeIsRequired, UserTaskMetadata metadata, Element parameterElement, Element element) {
		String target = parameterElement.attributeValue("target");
		if(StringUtil.isEmpty(target))
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">"+getXmlStruct()+"<parameter>的target属性值不能为空");
		
		element = element.element("candidate");
		if(element == null) 
			return new JumpOption(OptionTypeConstants.JUMP, name, order, target, suggestIsRequired, attitudeIsRequired, null);
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(metadata, getXmlStruct(), element);
		return new JumpOption(OptionTypeConstants.JUMP, name, order, target, suggestIsRequired, attitudeIsRequired, new Candidate(assignPolicy, null));
	}
}
