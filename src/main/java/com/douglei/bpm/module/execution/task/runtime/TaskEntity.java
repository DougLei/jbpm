package com.douglei.bpm.module.execution.task.runtime;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineBugException;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.mapping.ProcessMappingContainer;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadata;
import com.douglei.bpm.process.mapping.metadata.TaskMetadataEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class TaskEntity {
	private Task task;
	private ProcessMappingContainer container;
	private ProcessMetadata processMetadata;
	private TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity;
	
	public TaskEntity(int taskId, ProcessMappingContainer container) {
		this.task = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where id=?", Arrays.asList(taskId));
		if(task == null)
			throw new TaskHandleException("不存在id为["+taskId+"]的任务");
		this.container = container;
	}
	
	public TaskEntity(String taskinstId, ProcessMappingContainer container) {
		this.task = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where taskinst_id=?", Arrays.asList(taskinstId));
		if(task == null)
			throw new TaskHandleException("不存在taskinst_id为["+taskinstId+"]的任务");
		this.container = container;
	}
	
	/**
	 * 获取流程实例状态
	 * @return
	 */
	public State getProcessInstanceState() {
		return State.valueOf(Integer.parseInt(
				SessionContext.getSqlSession().uniqueQuery_(
						"select state from bpm_ru_procinst where procinst_id=?", Arrays.asList(task.getProcinstId()))[0].toString()));
	}
	
	/**
	 * 任务是否处于活动状态
	 * @return
	 */
	public boolean isActive() {
		State processInstanceState = getProcessInstanceState();
		switch(processInstanceState) {
			case ACTIVE:
				return task.isActive();
			case SUSPENDED:
				return false;
			default:
				throw new ProcessEngineBugException("判断任务是否处于活动状态时, 出现了错误的流程实例状态["+processInstanceState+"]");
		}
	}
	
	/**
	 * 获取流程元数据实例
	 * @return
	 */
	public ProcessMetadata getProcessMetadata() {
		if(processMetadata == null)
			processMetadata = container.getProcess(task.getProcdefId());
		return processMetadata;
	}
	/**
	 * 获取任务元数据实例
	 * @return
	 */
	public TaskMetadataEntity<? extends TaskMetadata> getTaskMetadataEntity() {
		if(taskMetadataEntity == null)
			taskMetadataEntity = getProcessMetadata().getTaskMetadataEntity(task.getKey());
		return taskMetadataEntity;
	}
	/**
	 * 获取任务实例
	 * @return
	 */
	public Task getTask() {
		return task;
	}
	
	/**
	 * 获取任务name
	 * @return
	 */
	public String getName() {
		return task.getName();
	}
	/**
	 * 是否是用户任务
	 * @return
	 */
	public boolean isUserTask() {
		return getTaskMetadataEntity().getTaskMetadata().getType() == Type.USER_TASK;
	}
}
