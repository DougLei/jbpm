package com.douglei.bpm.module.execution.task.command;

import java.util.Arrays;
import java.util.Date;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
import com.douglei.bpm.process.handler.task.user.timelimit.TimeLimitCalculator;
import com.douglei.bpm.process.handler.task.user.timelimit.TimeLimitCalculatorFactory;
import com.douglei.bpm.process.mapping.metadata.task.user.TimeLimit;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class WakeTaskCmd implements Command {
	private TaskEntity entity;
	
	public WakeTaskCmd(TaskEntity entity) {
		this.entity = entity;
	}
	
	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		Task task = entity.getTask();
		if(task.isActive())
			return new Result("唤醒失败, [%s]任务已处于活动状态", "jbpm.taskinst.wake.fail.state.error", task.getName());
		
		if(entity.getProcessInstanceState() == State.SUSPENDED)
			return new Result("唤醒失败, [%s]任务所在的流程实例处于挂起状态", "jbpm.taskinst.wake.fail.procinst.is.suspended", task.getName());
		
		// 如果任务的截止日期不为空, 需要重新计算截止日期
		Date expiryTime = task.getExpiryTime();
		if(expiryTime != null) { 
			TimeLimit timeLimit = entity.getTaskMetadataEntity().getTaskMetadata().getTimeLimit();
			TimeLimitCalculator calc = TimeLimitCalculatorFactory.build(timeLimit.getType());
			expiryTime = calc.getExpiryTime(expiryTime, task.getStartTime(), task.getSuspendTime(), new Date(), timeLimit.getTimes());
		}
		
		SessionContext.getSqlSession().executeUpdate(
				"update bpm_ru_task set expiry_time=?, suspend_time=null where id=?", 
				Arrays.asList(expiryTime, task.getId()));
		return Result.getDefaultSuccessInstance();
	}
}
