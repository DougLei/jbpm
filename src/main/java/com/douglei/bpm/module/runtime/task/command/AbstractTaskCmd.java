package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;

import com.douglei.bpm.module.runtime.task.HandleState;
import com.douglei.bpm.module.runtime.task.TaskEntity;
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
				Arrays.asList(entity.getTask().getTaskinstId(), userId, HandleState.CLAIMED.name()))[0].toString());
		return count > 0;
	}
}
