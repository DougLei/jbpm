package com.douglei.bpm.process.handler.event.start;

import java.util.Date;
import java.util.UUID;

import com.douglei.bpm.module.runtime.instance.StartParameter;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.TaskEntityHandler;
import com.douglei.bpm.process.handler.UserEntity;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;

/**
 * 
 * @author DougLei
 */
public class StartEventHandleParameter implements HandleParameter {
	private Date currentDate = new Date();
	private ProcessMetadata processMetadata; 
	private StartParameter parameter;
	private String processInstanceId;
	private UserEntity userEntity; // 办理的用户实体
	private TaskEntityHandler taskEntityHandler = new TaskEntityHandler(); // 任务实体处理器
	
	public StartEventHandleParameter(ProcessMetadata processMetadata, StartParameter parameter) {
		this.processMetadata = processMetadata;
		this.parameter = parameter;
		this.processInstanceId = UUID.randomUUID().toString();
		this.userEntity = new UserEntity(parameter.getUserId(), parameter.getAssignedUserIds());
	}
	
	@Override
	public Date getCurrentDate() {
		return currentDate;
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
