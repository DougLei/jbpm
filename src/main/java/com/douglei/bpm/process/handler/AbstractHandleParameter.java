package com.douglei.bpm.process.handler;

import java.util.Date;
import java.util.LinkedList;

import com.douglei.bpm.module.runtime.task.Task;

/**
 * 办理参数抽象类
 * @author DougLei
 */
public abstract class AbstractHandleParameter implements HandleParameter {
	private Date currentDate = new Date(); // 当前时间
	protected ProcessEntity processEntity; // 流程实体
	protected LinkedList<Task> tasks; // 办理的任务集合
	protected UserEntity userEntity; // 办理的用户实体
	protected VariableEntities variableEntities; // 办理中的流程变量
	
	@Override
	public final Date getCurrentDate() {
		return currentDate;
	}
	@Override
	public final ProcessEntity getProcessEntity() {
		return processEntity;
	}
	@Override
	public void addTask(Task task) {
		if(tasks == null)
			tasks = new LinkedList<Task>();
		tasks.add(task);
	}
	@Override
	public final Task getTask() {
		return tasks.getLast();
	}
	@Override
	public final UserEntity getUserEntity() {
		return userEntity;
	}
	@Override
	public final VariableEntities getVariableEntities() {
		return variableEntities;
	}
}
