package com.douglei.bpm.process.handler;

import java.util.Arrays;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 任务处理器
 * @author DougLei
 */
public abstract class TaskHandler<TM extends TaskMetadata, HP extends HandleParameter> {
	protected BeanInstances beanInstances; 
	protected TM taskMetadata; // 任务元数据实例
	protected HP handleParameter; // 办理参数
	
	// 设置参数
	final void setParameters(BeanInstances beanInstances, TM taskMetadata, HP handleParameter) {
		this.beanInstances = beanInstances;
		this.taskMetadata = taskMetadata;
		this.handleParameter = handleParameter;
	}
	
	/**
	 * 创建任务
	 * @param isSave 是否直接保存到任务运行表
	 * @return
	 */
	protected final Task createTask(boolean isSave) {
		Task task = new Task(
				handleParameter.getProcessEntity().getProcessMetadata().getId(), 
				handleParameter.getProcessEntity().getProcinstId(),
				handleParameter.getTask().getParentTaskinstId(),
				taskMetadata);
		if(isSave)
			SessionContext.getTableSession().save(task);
		return task;
	}
	
	/**
	 * 完成任务
	 * @return 
	 */
	protected final void completeTask() {
		// 从运行任务表中删除任务
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_task where id = ?", Arrays.asList(handleParameter.getTask().getId()));	

		// 将任务存到历史表	
		HistoryTask historyTask = new HistoryTask(handleParameter.getTask());	
		SessionContext.getTableSession().save(historyTask);	
		
		// 变量调度
		beanInstances.getVariableScheduler().followTaskDispatch(handleParameter.getProcessEntity().getProcinstId(), handleParameter.getTask().getTaskinstId());
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
