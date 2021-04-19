package com.douglei.bpm.module.execution.task.command;

import java.util.Arrays;

import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
abstract class AbstractTaskCmd {
	protected TaskEntity entity;
	
	public AbstractTaskCmd(TaskEntity entity) {
		this.entity = entity;
	}

	/**
	 * 判断指定id的用户是否认领了当前任务
	 * @param userId
	 * @return
	 */
	protected final boolean isClaimed(String userId) {
		int count = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_assignee where taskinst_id=? and user_id=? and handle_state=?", 
				Arrays.asList(entity.getTask().getTaskinstId(), userId, HandleState.CLAIMED.getValue()))[0].toString());
		return count > 0;
	}
}
