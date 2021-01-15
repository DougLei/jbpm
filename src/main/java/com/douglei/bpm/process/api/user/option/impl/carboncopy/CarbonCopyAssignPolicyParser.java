package com.douglei.bpm.process.api.user.option.impl.carboncopy;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.metadata.task.user.option.carboncopy.CarbonCopyAssignPolicy;
import com.douglei.bpm.process.parser.task.user.AssignPolicyParser;

/**
 * 
 * @author DougLei
 */
@Bean
public class CarbonCopyAssignPolicyParser extends AssignPolicyParser {

	@Override
	protected AssignPolicy parseAssignPolicy(String id, String name, String struct, Element element) {
		AssignNumber assignNumber = null; 
		boolean isDynamic = !"false".equalsIgnoreCase(element.attributeValue("isDynamic"));
		if(isDynamic) 
			assignNumber = parseAssignNumber(id, name, struct, element);
		
		return new CarbonCopyAssignPolicy(isDynamic, assignNumber);
	}
}
