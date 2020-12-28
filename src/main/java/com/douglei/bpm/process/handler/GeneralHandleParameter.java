package com.douglei.bpm.process.handler;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.module.runtime.task.TaskEntity;
import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.orm.context.SessionContext;

/**
 * 通用的办理参数
 * @author DougLei
 */
public class GeneralHandleParameter extends AbstractHandleParameter{
	
	protected GeneralHandleParameter() {
	}
	public GeneralHandleParameter(TaskEntity taskEntity, User handledUser, String suggest, Attitude attitude, List<User> assignedUsers) {
		addTask(taskEntity.getTask());
		super.processInstanceId = taskEntity.getTask().getProcinstId();
		super.processMetadata = taskEntity.getProcessMetadata();
		super.userEntity = new UserEntity(handledUser, suggest, attitude, assignedUsers);
		super.variableEntities = new VariableEntities(SessionContext.getTableSession().query(
				Variable.class, 
				"select * from bpm_ru_variable where procinst_id=? and (taskinst_id=? or scope =?)", 
				Arrays.asList(taskEntity.getTask().getProcinstId(), taskEntity.getTask().getTaskinstId(), Scope.GLOBAL.name())));
	}
}
