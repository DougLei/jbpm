package com.douglei.bpm.process.api.user.option.impl.delegate;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.option.delegate.DelegateAssignPolicy;
import com.douglei.bpm.process.mapping.parser.task.user.AssignPolicyParser;

/**
 * 
 * @author DougLei
 */
@Bean
public class DelegateAssignPolicyParser extends AssignPolicyParser {

	@Override
	protected AssignPolicy parseAssignPolicy(String id, String name, String struct, Element element) {
		return new DelegateAssignPolicy();
	}
}
