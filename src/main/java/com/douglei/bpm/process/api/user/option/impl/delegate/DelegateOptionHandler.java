package com.douglei.bpm.process.api.user.option.impl.delegate;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.delegate.DelegateOption;
import com.douglei.bpm.process.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
@Bean(clazz = OptionHandler.class)
public class DelegateOptionHandler extends OptionHandler {
	public static final String TYPE = "delegate";
	
	@Autowired
	private DelegateAssignPolicyParser assignPolicyParser;
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Option parse(String name, int order, String userTaskId, String userTaskName, Element element) throws ProcessParseException {
		boolean reason = false;
		Element parameterElement = element.element("parameter");
		if(parameterElement != null)
			reason = "true".equalsIgnoreCase(parameterElement.attributeValue("reason"));
		
		element = element.element("candidate");
		if(element == null)
			return createOption(name, order, reason, null);
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(userTaskId, userTaskName, getXmlStruct() + "<candidate>", element);
		return createOption(name, order, reason, new Candidate(assignPolicy, null));
	}

	// 创建option实例
	protected Option createOption(String name, int order, boolean reason, Candidate candidate) {
		return new DelegateOption(TYPE, name, order, reason, candidate);
	}
}
