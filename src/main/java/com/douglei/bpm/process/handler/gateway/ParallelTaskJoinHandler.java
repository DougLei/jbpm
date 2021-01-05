package com.douglei.bpm.process.handler.gateway;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.GeneralTaskHandler;
import com.douglei.bpm.process.handler.TaskEntity;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 并行任务合并处理器
 * @author DougLei
 */
public class ParallelTaskJoinHandler extends GeneralTaskHandler{
	private TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity; // 当前处理的任务元数据实体实例
	private TaskEntity previousTaskEntity; // 上一个办理的任务实体实例
	private String currentTaskParentTaskinstId; // 当前任务需关联的父任务实例id值
	
	public ParallelTaskJoinHandler(TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity, TaskEntity previousTaskEntity) {
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.previousTaskEntity = previousTaskEntity;
	}

	/**
	 * 进行join操作
	 * @return 是否完成合并
	 */
	public boolean join() {
		String parentTaskinstId = previousTaskEntity.getTask().getParentTaskinstId();
		if(parentTaskinstId == null) {
			if(previousTaskEntity.isCreateBranch()) // 如果上一个任务会创建分支, 则这里要记录上一个任务的实例id, 作为当前任务的父任务, 例如两个并行网关连在一起
				this.currentTaskParentTaskinstId = previousTaskEntity.getTask().getTaskinstId();
			return true;
		}
		return join(Arrays.asList(parentTaskinstId), false);
	}
	
	/**
	 * 进行join操作
	 * @param parentTaskinstId
	 * @param isRecursive 是否递归, 如果是递归, 则在处理当前任务前进行终止
	 * @return 是否完成合并
	 */
	private synchronized boolean join(List<Object> parentTaskinstId, boolean isRecursive) {
		Task parentTask = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where taskinst_id=?", parentTaskinstId);
		if(parentTask == null) // 已经被处理, 当前任务忽视即可
			return false;
		
		List<Task> tasks = SessionContext.getTableSession().query(Task.class, "select key_ from bpm_ru_task where parent_taskinst_id=?", parentTaskinstId);
		if(tasks.isEmpty()) { // 所有分支任务均完成
			completeTask(parentTask);
			if(parentTask.getParentTaskinstId() != null) 
				join(Arrays.asList(parentTask.getParentTaskinstId()), true);
		}
		if(isRecursive)
			return false; // 终止递归
		
		List<FlowMetadata> inputFlows = currentTaskMetadataEntity.getInputFlows();
		if(!tasks.isEmpty() && inputFlows.size() > 1) { // 当还有未完成的任务, 且当前任务有多条流入flow时, 需判断是否完成合并
			for (Task task : tasks) {
				for (FlowMetadata flow : inputFlows) {
					if(task.getKey().equals(flow.getSource()))
						return false;
				}
			}
		}
		
		// 如果父任务分出的任务数量等于合并的数量, 则currentTaskParentTaskinstId= parentTask.getParentTaskinstId; 否则, currentTaskParentTaskinstId= parentTask.getTaskinstId
		if(parentTask.getChildrenNum() == inputFlows.size())
			this.currentTaskParentTaskinstId = parentTask.getParentTaskinstId();
		else
			this.currentTaskParentTaskinstId = parentTask.getTaskinstId();
		return true;
	}
	
	/**
	 * 获取当前任务需关联的父任务实例id值
	 * @return
	 */
	public String getCurrentTaskParentTaskinstId() {
		return currentTaskParentTaskinstId;
	}

//	select 'x' b from sys_user
}
