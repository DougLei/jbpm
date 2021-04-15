package com.douglei.bpm.process.api.user.option.impl.carboncopy;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignNumber;
import com.douglei.bpm.process.mapping.metadata.task.user.candidate.assign.AssignPolicy;
import com.douglei.bpm.process.mapping.metadata.task.user.option.carboncopy.CarbonCopyAssignPolicy;
import com.douglei.bpm.process.mapping.parser.task.user.AssignPolicyParser;

/**
 * 
 * @author DougLei
 */
@Bean
public class CarbonCopyAssignPolicyParser extends AssignPolicyParser {

	@Override
	protected AssignPolicy parseAssignPolicy(UserTaskMetadata metadata, String struct, Element element) {
		AssignNumber assignNumber = parseAssignNumber(metadata, struct, element);
		return new CarbonCopyAssignPolicy(assignNumber);
	}
}
