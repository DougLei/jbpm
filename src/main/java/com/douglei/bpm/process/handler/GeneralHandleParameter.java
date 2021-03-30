package com.douglei.bpm.process.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 通用的办理参数
 * @author DougLei
 */
public class GeneralHandleParameter implements HandleParameter {
	private Date currentDate = new Date();
	private TaskInstance taskInstance;
	private String businessId;
	private UserEntity userEntity; // 办理的用户实体
	private TaskEntityHandler taskEntityHandler = new TaskEntityHandler(); // 任务实体处理器
	private VariableEntities variableEntities;
	
	public GeneralHandleParameter(TaskInstance taskInstance, String currentHandleUserId, String suggest, Attitude attitude, String reason, String businessId, HashSet<String> assignedUserIds) {
		this.taskInstance = taskInstance;
		this.taskEntityHandler.setCurrentTaskEntity(new TaskEntity(taskInstance.getTask()));
		this.businessId = businessId;
		this.userEntity = new UserEntity(currentHandleUserId, suggest, attitude, reason, assignedUserIds);
	}
	
	@Override
	public Date getCurrentDate() {
		return currentDate;
	}
	@Override
	public String getProcessInstanceId() {
		return taskInstance.getTask().getProcinstId();
	}
	@Override
	public String getBusinessId() {
		return businessId;
	}
	@Override
	public ProcessMetadata getProcessMetadata() {
		return taskInstance.getProcessMetadata();
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
					taskInstance.getTask().getTaskinstId(), 
					SessionContext.getTableSession().query(Variable.class, "select * from bpm_ru_variable where procinst_id=? and (taskinst_id=? or scope =?)", Arrays.asList(taskInstance.getTask().getProcinstId(), taskInstance.getTask().getTaskinstId(), Scope.GLOBAL.name())));
		return variableEntities;
	}
}
