package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.ProcessMetadata;

/**
 * 办理参数抽象类
 * @author DougLei
 */
public abstract class AbstractHandleParameter implements HandleParameter {
	private Date currentDate = new Date(); // 当前时间
	protected String processInstanceId; // 流程实例id
	protected ProcessMetadata processMetadata; // 流程元数据实例
	protected List<Task> tasks; // 办理的任务集合
	protected UserEntity userEntity; // 办理的用户实体
	protected VariableEntities variableEntities; // 办理中的流程变量
	
	@Override
	public final Date getCurrentDate() {
		return currentDate;
	}
	@Override
	public final String getProcessInstanceId() {
		return processInstanceId;
	}
	@Override
	public final ProcessMetadata getProcessMetadata() {
		return processMetadata;
	}
	@Override
	public void addTask(Task task) {
		if(tasks == null)
			tasks = new ArrayList<Task>(5);
		tasks.add(task);
	}
	@Override
	public Task getPreviousTask() {
		if(tasks.size() == 1)
			return tasks.get(0);
		return tasks.get(tasks.size()-2);
	}
	@Override
	public Task getCurrentTask() {
		if(tasks == null)
			return null;
		return tasks.get(tasks.size()-1);
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
