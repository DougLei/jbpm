package com.douglei.bpm.module.execution.instance.command;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstance;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstanceEntity;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.process.handler.task.user.timelimit.TimeLimitCalculator;
import com.douglei.bpm.process.handler.task.user.timelimit.TimeLimitCalculatorFactory;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.TimeLimit;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class WakeProcessCmd implements Command {
	protected ProcessInstance processInstance;
	
	public WakeProcessCmd() {}
	public WakeProcessCmd(ProcessInstanceEntity entity) {
		this.processInstance = entity.getProcessInstance();
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance() != State.SUSPENDED)
			return new Result("唤醒失败, [%s]流程实例未处于[SUSPENDED]状态", "jbpm.procinst.wake.fail.state.error", processInstance.getTitle());
		
		// 批量计算任务的截止日期
		List<Task> tasks = SessionContext.getTableSession().query(Task.class, 
				"select id, key_, start_time, expiry_time from bpm_ru_task where procinst_id=? and suspend_time=null and expiry_time <> null", 
				Arrays.asList(processInstance.getProcinstId()));
		
		if(!tasks.isEmpty()) {
			updateExpiryTimes(processInstance.getProcdefId(), processInstance.getSuspendTime(), tasks, processEngineBeans);
			tasks.forEach(task -> {
				SessionContext.getSqlSession().executeUpdate("update bpm_ru_task set expiry_time=? where id=?", Arrays.asList(task.getExpiryTime(), task.getId()));
			});
		}
		
		SessionContext.getSqlSession().executeUpdate("update bpm_ru_procinst set state=?, suspend_time=null where id=?", Arrays.asList(State.ACTIVE.getValue(), processInstance.getId()));
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 批量更新任务的截止日期
	 * @param procdefId 流程定义id
	 * @param procinstSuspendTime 流程实例的挂起时间
	 * @param tasks 相关的任务集合
	 * @param processEngineBeans
	 */
	void updateExpiryTimes(int procdefId, Date procinstSuspendTime, List<Task> tasks, ProcessEngineBeans processEngineBeans) {
		Date currentDate = new Date();
		ProcessMetadata processMetadata = processEngineBeans.getProcessContainer().getProcess(procdefId);
		Map<String, TaskEntity> cache = new HashMap<String, TaskEntity>(8);
		
		tasks.forEach(task -> {
			TaskEntity entity = cache.get(task.getKey());
			if(entity == null) {
				TimeLimit timeLimit = processMetadata.getTaskMetadataEntity(task.getKey()).getTaskMetadata().getTimeLimit();
				entity = new TaskEntity(timeLimit.getTimes(), TimeLimitCalculatorFactory.build(timeLimit.getType()));
				cache.put(task.getKey(), entity);
			}
			task.setExpiryTime(entity.calc.getExpiryTime(task.getExpiryTime(), task.getStartTime(), procinstSuspendTime, currentDate, entity.times));
		});
	}
	
	/**
	 * 
	 * @author DougLei
	 */
	private class TaskEntity {
		long times;
		TimeLimitCalculator calc;
		
		public TaskEntity(long times, TimeLimitCalculator calc) {
			this.times = times;
			this.calc = calc;
		}
	}
}
