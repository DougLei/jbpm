package com.douglei.bpm.process.handler.event.start;

import java.util.UUID;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.UserEntity;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventHandleParameter extends GeneralHandleParameter {
	private StartParameter parameter;
	
	public StartEventHandleParameter(ProcessEngineBeans processEngineBeans, ProcessMetadata processMetadata, StartParameter parameter) {
		super.processInstanceId = UUID.randomUUID().toString();
		super.processMetadata = processMetadata;
		super.userEntity = new UserEntity(
				processEngineBeans.getUserBeanFactory().create(parameter.getUserId()), 
				processEngineBeans.getUserBeanFactory().create(parameter.getAssignUserIds()));
		super.variableEntities = parameter.getVariableEntities();
		this.parameter = parameter;
	}
	
	public String getBusinessId() {
		return parameter.getBusinessId();
	}
	public String getUserId() {
		return parameter.getUserId();
	}
	public String getTenantId() {
		return parameter.getTenantId();
	}
}
