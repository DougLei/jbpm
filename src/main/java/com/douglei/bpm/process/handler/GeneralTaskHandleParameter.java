package com.douglei.bpm.process.handler;

import java.util.Arrays;

import com.douglei.bpm.module.execution.task.history.Attitude;
import com.douglei.bpm.module.execution.variable.Scope;
import com.douglei.bpm.module.execution.variable.runtime.Variable;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 通用的任务办理参数
 * @author DougLei
 */
public class GeneralTaskHandleParameter extends AbstractHandleParameter {
	private com.douglei.bpm.module.execution.task.runtime.TaskEntity entity;
	private String businessId;
	private UserEntity userEntity; // 办理的用户实体
	private TaskEntityHandler taskEntityHandler = new TaskEntityHandler(); // 任务实体处理器
	private VariableEntities variableEntities;
	
	public GeneralTaskHandleParameter(com.douglei.bpm.module.execution.task.runtime.TaskEntity entity, String userId, String suggest, Attitude attitude, String reason, String businessId, AssignEntity assignEntity) {
		this.entity = entity;
		this.taskEntityHandler.setCurrentTaskEntity(new TaskEntity(entity.getTask()));
		this.businessId = businessId;
		this.userEntity = new UserEntity(userId, suggest, attitude, reason, assignEntity);
	}
	
	@Override
	public String getProcessInstanceId() {
		return entity.getTask().getProcinstId();
	}
	@Override
	public String getBusinessId() {
		return businessId;
	}
	@Override
	public ProcessMetadata getProcessMetadata() {
		return entity.getProcessMetadata();
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
		if(variableEntities == null)
			this.variableEntities = new VariableEntities(
					entity.getTask().getTaskinstId(), 
					SessionContext.getTableSession().query(Variable.class, "select * from bpm_ru_variable where procinst_id=? and (taskinst_id=? or scope =?)", Arrays.asList(entity.getTask().getProcinstId(), entity.getTask().getTaskinstId(), Scope.GLOBAL.getValue())));
		return variableEntities;
	}
}
