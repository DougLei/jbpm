package com.douglei.bpm.process.handler.gateway;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.GeneralTaskHandler;
import com.douglei.bpm.process.handler.TaskEntity;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 并行任务处理器
 * @author DougLei
 */
public class ParallelTaskHandler extends GeneralTaskHandler{
	private TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity; // 当前处理的任务元数据实体实例
	private TaskEntity previousTaskEntity; // 上一个办理的任务实体实例
	
	public ParallelTaskHandler(TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity, TaskEntity previousTaskEntity) {
		this.currentTaskMetadataEntity = currentTaskMetadataEntity;
		this.previousTaskEntity = previousTaskEntity;
	}

	/**
	 * 进行join操作
	 * @return 
	 */
	public ParallelTaskJoinResult join() {
		String parentTaskinstId = previousTaskEntity.getTask().getParentTaskinstId();
		if(parentTaskinstId == null) {
			if(previousTaskEntity.isCreateBranch()) // 如果上一个任务会创建分支, 则这里要记录上一个任务的实例id, 作为当前任务的父任务, 例如两个并行网关连在一起
				
			return true;
		}
		return join(Arrays.asList(parentTaskinstId), false);
	}
	
	/**
	 * 进行join操作
	 * @param parentTaskinstId
	 * @param isRecursive 是否递归, 如果是递归, 则在处理当前任务前进行终止
	 * @return 
	 */
	private synchronized ParallelTaskJoinResult join(List<Object> parentTaskinstId, boolean isRecursive) {
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
		
		if(tasks.size() > 0 && canReachGateway(tasks))  // 判断当前还在运行的任务, 是否有可以到达当前网关的, 如果有, 则证明还未合并完成
			return false; 
		
		
		// TODO 又是怎样获取
		
		return true;
	}
	
	// 判断tasks中, 是否有可以到达当前网关的
	private boolean canReachGateway(List<Task> tasks) {
		Set<String> visitedTaskIds = new HashSet<String>();
		for (Task task : tasks) {
			if(canReachGateway(currentTaskMetadataEntity.getProcessMetadata().getTaskMetadataEntity(task.getKey()), visitedTaskIds))
				return true;
		}
		return false;
	}
	private boolean canReachGateway(TaskMetadataEntity<TaskMetadata> source, Set<String> visitedTaskIds) {
		if(visitedTaskIds.size() > 0 && visitedTaskIds.contains(source.getTaskMetadata().getId()))
			return false;
		visitedTaskIds.add(source.getTaskMetadata().getId());
		
		List<FlowMetadata> outFlows = source.getOutputFlows();
		if(outFlows.isEmpty())
			return false;
		
		for(FlowMetadata outputFlow : outFlows) {
			if(outputFlow.getTarget().equals(currentTaskMetadataEntity.getTaskMetadata().getId()))
				return true;
			if(canReachGateway(currentTaskMetadataEntity.getProcessMetadata().getTaskMetadataEntity(outputFlow.getTarget()), visitedTaskIds))
				return true;
		}
		return false;
	}
}
