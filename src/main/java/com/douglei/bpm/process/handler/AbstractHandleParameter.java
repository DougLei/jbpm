package com.douglei.bpm.process.handler;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.Task;

/**
 * 办理参数抽象类
 * @author DougLei
 */
public abstract class AbstractHandleParameter implements HandleParameter {
	private Date currentDate = new Date(); // 当前时间
	protected ProcessEntity processEntity; // 流程实体
	protected Task task; // 办理的任务实例
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
	public final Task getTaskInstance() {
		return task;
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
