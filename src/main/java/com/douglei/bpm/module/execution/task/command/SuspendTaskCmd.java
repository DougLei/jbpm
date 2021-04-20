package com.douglei.bpm.module.execution.task.command;

import java.util.Arrays;
import java.util.Date;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class SuspendTaskCmd implements Command {
	private Task task;
	
	public SuspendTaskCmd(TaskEntity entity) {
		this.task = entity.getTask();
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(task.isActive()) {
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_active=0, suspend_time=? where id=?", Arrays.asList(new Date(), task.getId()));
			return Result.getDefaultSuccessInstance();
		}
		return new Result("挂起失败, [%s]任务已处于挂起状态", "jbpm.taskinst.suspend.fail.state.error", task.getName());			
	}
}
