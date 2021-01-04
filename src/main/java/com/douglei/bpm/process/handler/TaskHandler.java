package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.history.variable.HistoryVariable;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.orm.context.SessionContext;

/**
 * 任务处理器
 * @author DougLei
 */
public abstract class TaskHandler<TM extends TaskMetadata, HP extends HandleParameter> {
	protected BeanInstances beanInstances; 
	protected TaskMetadataEntity<TM> currentTaskMetadataEntity; // 当前任务元数据实例
	protected HP handleParameter; // 办理参数
	
	// 设置参数
	final void setParameters(BeanInstances beanInstances, TaskMetadataEntity<TM> currentTaskMetadataEntity, HP handleParameter) {
		this.beanInstances = beanInstances;
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.handleParameter = handleParameter;
	}
	
	/**
	 * 创建任务
	 * @return
	 */
	protected final Task createTask() {
		return createTask(handleParameter.getPreviousTaskEntity().getTask().getParentTaskinstId()); // TODO 上一个任务是否会产生分支
	}
	/**
	 * 创建任务
	 * @param parentTaskinstId
	 * @return
	 */
	protected final Task createTask(String parentTaskinstId) {
		Task task = new Task(
				handleParameter.getProcessMetadata().getId(), 
				handleParameter.getProcessInstanceId(),
				parentTaskinstId,
				currentTaskMetadataEntity.getTaskMetadata());
		
		SessionContext.getTableSession().save(task);
		handleParameter.addTaskEntity(new TaskEntity(task));
		return task;
	}
	
	/**
	 * 创建历史任务
	 * @return
	 */
	protected final Task createHistoryTask() {
		return createHistoryTask(handleParameter.getPreviousTaskEntity().getTask().getParentTaskinstId());
	}
	/**
	 * 创建历史任务
	 * @param parentTaskinstId
	 * @return
	 */
	protected final HistoryTask createHistoryTask(String parentTaskinstId) {
		HistoryTask historyTask = new HistoryTask(
				handleParameter.getProcessMetadata().getId(), 
				handleParameter.getProcessInstanceId(),
				parentTaskinstId,
				currentTaskMetadataEntity.getTaskMetadata());
		SessionContext.getTableSession().save(historyTask);
		
		handleParameter.addTaskEntity(new TaskEntity(historyTask));
		return historyTask;
	}
	
	/**
	 * 完成任务
	 * @param task
	 */
	protected final void completeTask(Task task) {
		// 从运行任务表中删除任务
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_task where id = ?", Arrays.asList(task.getId()));	

		// 将任务存到历史表	
		HistoryTask historyTask = new HistoryTask(task);	
		SessionContext.getTableSession().save(historyTask);	
	}
	
	/**
	 * 跟随任务完成
	 * @param task
	 */
	protected final void followTaskCompleted(Task task) {
		VariableEntities variableEntities = new VariableEntities(SessionContext.getTableSession()
				.query(Variable.class, 
						"select * from bpm_ru_variable where procinst_id=? and taskinst_id=?", Arrays.asList(task.getProcinstId(), task.getTaskinstId())));
		
		// 将local和transient范围的变量从运行变量表删除	
		if(variableEntities.existsLocalVariable() || variableEntities.existsTransientVariable())	
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where taskinst_id = ?", Arrays.asList(task.getTaskinstId()));	
		
		// 将local范围的变量保存到历史变量表	
		if(variableEntities.existsLocalVariable()) {	
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(variableEntities.getLocalVariableMap().size());	
			variableEntities.getLocalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(task.getProcinstId(), task.getTaskinstId(), variableEntity));	
			});	
			SessionContext.getTableSession().save(historyVariables);	
		}	
	}
	
	/**
	 * 启动任务
	 * @return
	 */
	public abstract ExecutionResult startup();
	
	/**
	 * 办理任务
	 * @return
	 */
	public abstract ExecutionResult handle();
}
