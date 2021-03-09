package com.douglei.bpm.module.runtime.task.command.dispatch.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.bpm.ProcessEngineBugException;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.mapping.metadata.TaskNotExistsException;
import com.douglei.orm.context.SessionContext;

/**
 * 回退步数调度
 * @author DougLei
 */
public class BackstepsDispatchExecutor extends SettargetDispatchExecutor {
	private static final Logger logger = LoggerFactory.getLogger(BackstepsDispatchExecutor.class);
	private int steps; // 回退的步数, 默认值为1; 超过最大步数时使用最大步数

	public BackstepsDispatchExecutor() {
		this(1, false);
	}
	public BackstepsDispatchExecutor(int steps) {
		this(steps, false);
	}
	public BackstepsDispatchExecutor(int steps, boolean executeCC) {
		super(null, true, executeCC);
		if(steps < 1)
			throw new TaskDispatchException("回退步数调度时, 设置的步数值不能小于1");
		this.steps = steps;
	}

	@Override
	public void execute() throws TaskNotExistsException, TaskDispatchException {
		List<TaskEntity> historyTasks = SessionContext.getTableSession()
				.query(TaskEntity.class, "select taskinst_id, source_key, key_ from bpm_hi_task where procinst_id=? order by end_time desc", Arrays.asList(handleParameter.getProcessInstanceId()));
		
		TaskEntity currentTask = new TaskEntity(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask(), null);
		TaskEntity targetTask = getTargetTask(0, historyTasks, currentTask);
		if(targetTask == currentTask)
			throw new TaskDispatchException("不能回退到当前任务");
		
		execute(targetTask.getTaskinstId(), targetTask.getKey());
	}
	
	// 获取目标任务(key/id值)
	private TaskEntity getTargetTask(int index, List<TaskEntity> historyTasks, TaskEntity previousTask) {
		TaskEntity currentTask;
		for (; index<historyTasks.size(); index++) {
			currentTask = historyTasks.get(index);
			
			if(currentTask.getKey().equals(previousTask.getSourceKey())) {
				if(currentTask.isStartEvent(handleParameter.getProcessMetadata()))
					return previousTask;
				if(--steps == 0)
					return currentTask;
				return getTargetTask(++index, historyTasks, currentTask);
			}
		}
		
		logger.error("回退步数调度时, 无法获取目标任务的key值, 相关数据为: taskinstId={}, steps={}", handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(), steps);
		throw new ProcessEngineBugException("回退步数调度时, 无法获取目标任务的key值");
	}
}
