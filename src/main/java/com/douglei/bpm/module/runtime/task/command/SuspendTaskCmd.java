package com.douglei.bpm.module.runtime.task.command;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.TaskEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class SuspendTaskCmd extends WakeTaskCmd {
	
	public SuspendTaskCmd(TaskEntity entity) {
		super(entity);
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(task.isActive()) {
			SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set is_active=0 where id=?", Arrays.asList(task.getId()));
			return ExecutionResult.getDefaultSuccessInstance();
		}
		return new ExecutionResult("挂起失败, [%s]任务已处于挂起状态", "jbpm.taskinst.suspend.fail.state.error", task.getName());			
	}
}
