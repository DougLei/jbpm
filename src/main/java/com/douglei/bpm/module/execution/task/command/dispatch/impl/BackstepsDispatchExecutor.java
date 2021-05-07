package com.douglei.bpm.module.execution.task.command.dispatch.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.bpm.ProcessEngineBugException;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.execution.task.command.dispatch.DispatchExecutor;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.orm.context.SessionContext;

/**
 * 回退步数调度
 * @author DougLei
 */
public class BackstepsDispatchExecutor extends DispatchExecutor {
	private static final Logger logger = LoggerFactory.getLogger(BackstepsDispatchExecutor.class);
	private int steps; // 回退的步数; 超过最大步数时使用最大步数
	
	public BackstepsDispatchExecutor(int steps) {
		if(steps < 1)
			throw new TaskDispatchException("回退步数调度时, 设置的步数值不能小于1");
		this.steps = steps;
	}
	
	@Override
	protected DispatchResult parse(HashSet<String> assignedUserIds) {
		TaskEntity targetTask = getTargetTask();
		return new DispatchResult(targetTask.getKey(), getAssignedUsers(targetTask));
	}
	
	// *获取要回退到的目标任务实体
	private TaskEntity getTargetTask() {
		List<TaskEntity> historyTasks = SessionContext.getTableSession()
				.limitQuery(TaskEntity.class, 2, -1, "select taskinst_id, parent_taskinst_id, source_key, key_ from bpm_hi_task where procinst_id=? order by end_time desc", Arrays.asList(handleParameter.getProcessInstanceId()));
		
		TaskEntity currentTask = new TaskEntity(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask(), null);
		TaskEntity targetTask = getTargetTask(0, currentTask, historyTasks);
		if(targetTask == currentTask)
			throw new TaskDispatchException("不能回退到当前任务");
		return targetTask;
	}
	
	// 获取目标任务(key/id值)
	private TaskEntity getTargetTask(int index, TaskEntity previousTask, List<TaskEntity> historyTasks) {
		for (; index<historyTasks.size(); index++) {
			TaskEntity currentTask = historyTasks.get(index);
			
			if(currentTask.getKey().equals(previousTask.getSourceKey())) {
				if(currentTask.isStartEvent(handleParameter.getProcessMetadata()))
					return previousTask;
				if(--steps == 0)
					return currentTask;
				return getTargetTask(++index, currentTask, historyTasks);
			}
		}
		
		logger.error("回退步数调度时, 无法获取目标任务的key值, 相关数据: taskinstId={}, steps={}", handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask().getTaskinstId(), steps);
		throw new ProcessEngineBugException("回退步数调度时, 无法获取目标任务的key值");
	}
	
	// 获取指派的用户id集合
	private HashSet<String> getAssignedUsers(TaskEntity targetTask) {
		List<Object[]> list = SessionContext.getSqlSession().query_("select distinct user_id from bpm_hi_assignee where taskinst_id=? and handle_state=?", Arrays.asList(targetTask.getTaskinstId(), HandleState.FINISHED.getValue()));
		if(list.isEmpty())
			return null;
		
		HashSet<String> assignedUserIds = new HashSet<String>();
		list.forEach(array -> assignedUserIds.add(array[0].toString()));
		return assignedUserIds;
	}
}
