package com.douglei.bpm.process.handler.event.start;

import java.util.UUID;

import com.douglei.bpm.module.execution.instance.command.parameter.StartParameter;
import com.douglei.bpm.process.handler.AbstractHandleParameter;
import com.douglei.bpm.process.handler.TaskEntityHandler;
import com.douglei.bpm.process.handler.UserEntity;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventHandleParameter extends AbstractHandleParameter {
	private ProcessMetadata processMetadata; 
	private StartParameter parameter;
	private String processInstanceId;
	private UserEntity userEntity; // 办理的用户实体
	private TaskEntityHandler taskEntityHandler = new TaskEntityHandler(); // 任务实体处理器
	
	public StartEventHandleParameter(ProcessMetadata processMetadata, StartParameter parameter) {
		this.processMetadata = processMetadata;
		this.parameter = parameter;
		this.processInstanceId = UUID.randomUUID().toString();
		this.userEntity = new UserEntity(parameter.getUserId(), null, null, null, parameter.getAssignEntity());
	}
	
	@Override
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	@Override
	public String getBusinessId() {
		return parameter.getBusinessId();
	}
	@Override
	public ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	@Override
	public TaskEntityHandler getTaskEntityHandler() {
		return taskEntityHandler;
	}
	@Override
	public UserEntity getUserEntity() {
		return userEntity;
	}
	@Override
	public VariableEntities getVariableEntities() {
		return parameter.getVariableEntities();
	}
	
	/**
	 * 获取租户id
	 * @return
	 */
	public String getTenantId() {
		return parameter.getTenantId();
	}
}
