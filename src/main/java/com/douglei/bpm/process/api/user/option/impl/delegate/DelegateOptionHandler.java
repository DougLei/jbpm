package com.douglei.bpm.process.api.user.option.impl.delegate;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.metadata.task.user.candidate.Candidate;
import com.douglei.bpm.process.metadata.task.user.option.Option;
import com.douglei.bpm.process.metadata.task.user.option.delegate.DelegateOption;
import com.douglei.bpm.process.parser.ProcessParseException;

/**
 * 
 * @author DougLei
 */
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
		element = element.element("candidate");
		if(element == null)
			return createOption(name, order, null);
		return createOption(name, order, new Candidate(assignPolicyParser.parse(userTaskId, userTaskName, getXmlStruct() + "<candidate>", element), null));
	}

	// 创建option实例
	protected Option createOption(String name, int order, Candidate candidate) {
		return new DelegateOption(TYPE, name, order, candidate);
	}
}
