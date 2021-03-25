package com.douglei.bpm.process.handler.event.start;

import java.util.UUID;

import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.process.handler.GeneralHandleParameter;
import com.douglei.bpm.process.handler.UserEntity;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventHandleParameter extends GeneralHandleParameter {
	private String tenantId;
	
	public StartEventHandleParameter(ProcessMetadata processMetadata, StartParameter parameter) {
		this.processInstanceId = UUID.randomUUID().toString();
		this.businessId = parameter.getBusinessId();
		this.processMetadata = processMetadata;
		this.tenantId = parameter.getTenantId();
		
		this.userEntity = new UserEntity(parameter.getUserId(), parameter.getAssignedUserIds());
		this.variableEntities = parameter.getVariableEntities();
	}
	
	/**
	 * 获取租户id
	 * @return
	 */
	public String getTenantId() {
		return tenantId;
	}
}
