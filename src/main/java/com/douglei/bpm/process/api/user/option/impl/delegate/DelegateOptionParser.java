package com.douglei.bpm.process.api.user.option.impl.delegate;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.AbstractOptionParser;
import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.metadata.task.user.option.delegate.DelegateOption;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
@Bean(clazz = AbstractOptionParser.class)
public class DelegateOptionParser extends AbstractOptionParser {
	
	@Autowired
	private DelegateAssignPolicyParser assignPolicyParser;
	
	@Override
	public String getType() {
		return OptionTypeConstants.DELEGATE;
	}
	
	@Override
	public Option parse(String name, int order, UserTaskMetadata metadata, Element element) throws ProcessParseException {
		boolean reasonIsRequired = false;
		Element parameterElement = element.element("parameter");
		if(parameterElement != null)
			reasonIsRequired = "true".equalsIgnoreCase(parameterElement.attributeValue("reason"));
		
		element = element.element("candidate");
		if(element == null)
			return createOption(name, order, reasonIsRequired, null);
		
		AssignPolicy assignPolicy = assignPolicyParser.parse(metadata, getXmlStruct() + "<candidate>", element);
		return createOption(name, order, reasonIsRequired, new Candidate(assignPolicy, null));
	}

	// 创建option实例
	protected Option createOption(String name, int order, boolean reasonIsRequired, Candidate candidate) {
		return new DelegateOption(OptionTypeConstants.DELEGATE, name, order, reasonIsRequired, candidate);
	}
}
