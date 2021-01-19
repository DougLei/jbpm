package com.douglei.bpm.process.handler;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.history.task.Attitude;
import com.douglei.bpm.module.runtime.task.TaskInstance;
import com.douglei.bpm.module.runtime.variable.Scope;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.api.user.bean.factory.UserBean;
import com.douglei.orm.context.SessionContext;

/**
 * 通用的办理参数
 * @author DougLei
 */
public class GeneralHandleParameter extends AbstractHandleParameter{
	
	protected GeneralHandleParameter() {}
	public GeneralHandleParameter(TaskInstance taskInstance, UserBean currentHandleUser, String suggest, Attitude attitude, String reason, List<UserBean> assignedUsers) {
		super.processInstanceId = taskInstance.getTask().getProcinstId();
		super.processMetadata = taskInstance.getProcessMetadata();
		super.taskEntityHandler.setCurrentTaskEntity(new TaskEntity(taskInstance.getTask()));
		super.userEntity = new UserEntity(currentHandleUser, suggest, attitude, reason, assignedUsers);
		super.variableEntities = new VariableEntities(SessionContext.getTableSession().query(
				Variable.class, 
				"select * from bpm_ru_variable where procinst_id=? and (taskinst_id=? or scope =?)", 
				Arrays.asList(taskInstance.getTask().getProcinstId(), taskInstance.getTask().getTaskinstId(), Scope.GLOBAL.name())));
	}
}
