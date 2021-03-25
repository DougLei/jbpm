package com.douglei.bpm.process.handler;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.orm.context.SessionContext;

/**
 * 通用的办理参数
 * @author DougLei
 */
public class GeneralHandleParameter extends AbstractHandleParameter{
	
	protected GeneralHandleParameter() {}
	public GeneralHandleParameter(TaskInstance taskInstance, String currentHandleUserId, String suggest, Attitude attitude, String reason, String businessId, List<String> assignedUserIds) {
		this.processInstanceId = taskInstance.getTask().getProcinstId();
		this.businessId = businessId;
		this.processMetadata = taskInstance.getProcessMetadata();
		
		this.taskEntityHandler.setCurrentTaskEntity(new TaskEntity(taskInstance.getTask()));
		this.userEntity = new UserEntity(currentHandleUserId, suggest, attitude, reason, assignedUserIds);
		this.variableEntities = new VariableEntities(
				taskInstance.getTask().getTaskinstId(), 
				SessionContext.getTableSession().query(Variable.class, "select * from bpm_ru_variable where procinst_id=? and (taskinst_id=? or scope =?)", Arrays.asList(taskInstance.getTask().getProcinstId(), taskInstance.getTask().getTaskinstId(), Scope.GLOBAL.name())));
	}
}
