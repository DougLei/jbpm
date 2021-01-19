package com.douglei.bpm.process.api.user.option.impl.carboncopy;

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
	protected AssignPolicy createInstance(boolean isDynamic, AssignNumber assignNumber) {
		return new CarbonCopyAssignPolicy(isDynamic, assignNumber);
	}
}
