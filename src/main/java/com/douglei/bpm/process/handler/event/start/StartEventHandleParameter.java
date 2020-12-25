package com.douglei.bpm.process.handler.event.start;

import java.util.UUID;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.ProcessEntity;
import com.douglei.bpm.process.handler.UserEntity;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventHandleParameter extends GeneralHandleParameter {
	private StartParameter parameter;
	
	public StartEventHandleParameter(BeanInstances beanInstances, ProcessMetadata processMetadata, StartParameter parameter) {
		super.processEntity = new ProcessEntity(UUID.randomUUID().toString(), processMetadata);
		super.userEntity = new UserEntity(beanInstances.getUserFactory().create(parameter.getUserId()), beanInstances.getUserFactory().create(parameter.getAssignedUserIds()));
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
