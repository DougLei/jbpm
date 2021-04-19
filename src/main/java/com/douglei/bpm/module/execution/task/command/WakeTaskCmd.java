package com.douglei.bpm.module.execution.task.command;

import java.util.Arrays;

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
public class WakeTaskCmd implements Command {
	protected Task task;
	
	public WakeTaskCmd(TaskEntity entity) {
		this.task = entity.getTask();
	}
	
	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(task.isActive())
			return new Result("唤醒失败, [%s]任务已处于活动状态", "jbpm.taskinst.wake.fail.state.error", task.getName());
		
		SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_active=1 where id=?", Arrays.asList(task.getId()));
		return Result.getDefaultSuccessInstance();
	}
}
