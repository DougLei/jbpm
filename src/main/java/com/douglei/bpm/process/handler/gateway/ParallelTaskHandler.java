package com.douglei.bpm.process.handler.gateway;

import java.util.Arrays;
import java.util.Date;
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
	private TaskMetadataEntity<? extends TaskMetadata> currentJoinTaskMetadataEntity; // 当前处理join的任务元数据实体实例
	private TaskEntity previousTaskEntity; // 上一个办理的任务实体实例
	private Date currentDate; // 当前操作时间
	
	public ParallelTaskHandler(TaskMetadataEntity<? extends TaskMetadata> currentJoinTaskMetadataEntity, TaskEntity previousTaskEntity, Date currentDate) {
		this.currentJoinTaskMetadataEntity = currentJoinTaskMetadataEntity;
		this.previousTaskEntity = previousTaskEntity;
		this.currentDate = currentDate;
	}

	/**
	 * 进行join操作
	 * @return 进行join的任务实例(joinTask); 返回null表示join未完成
	 */
	public Task join() {
		String parentTaskinstId = previousTaskEntity.getTask().getParentTaskinstId();
		if(parentTaskinstId == null) {
			Task joinTask = new Task(
					previousTaskEntity.getTask().getProcinstId(), 
					null, 
					currentDate, 
					previousTaskEntity.getTask().getKey(),
					currentJoinTaskMetadataEntity.getTaskMetadata(), 
					currentJoinTaskMetadataEntity.getProcessMetadata());
			joinTask.setJoinBranchNum(1);
			
			if(previousTaskEntity.isCreateBranch()) // 如果上一个任务会创建分支, 则这里要记录上一个任务的实例id, 作为当前任务的父任务, 例如两个并行网关连在一起
				joinTask.setParentTaskinstId(previousTaskEntity.getTask().getTaskinstId());
			return joinTask;
		}
		return join(Arrays.asList(parentTaskinstId), false);
	}
	
	/**
	 * 进行join操作
	 * @param parentTaskinstId
	 * @param isRecursive 是否递归, 如果是递归, 则在处理当前任务前进行终止
	 * @return 
	 */
	private synchronized Task join(List<Object> parentTaskinstId, boolean isRecursive) {
		Task parentTask = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where taskinst_id=?", parentTaskinstId);
		if(parentTask == null) // 已经被处理, 当前任务忽视即可
			return null;
		
		ParallelTaskHolder parallelTaskHolder = new ParallelTaskHolder(parentTaskinstId, !isRecursive);
		if(parallelTaskHolder.tasks.isEmpty()) { // 所有分支任务均完成
			completeTask(parentTask, currentDate, null);
			if(parentTask.getParentTaskinstId() != null) 
				join(Arrays.asList(parentTask.getParentTaskinstId()), true);
		}
		if(isRecursive)
			return null; // 终止递归
		
		// 判断并行任务集合, 是否有可以到达当前网关的, 如果有, 则证明还未合并完成
		Task joinTask = parallelTaskHolder.joinTask;
		if(parallelTaskHolder.tasks.size() > 0 && currentJoinTaskMetadataEntity.getInputFlows().size() > 1 && canReachGateway(parallelTaskHolder.tasks)) {
			if(joinTask.getId() == 0) { // 第一次join, 进行保存
				SessionContext.getTableSession().save(joinTask);
			}else {
				SessionContext.getSqlSession().executeUpdate(
						"update bpm_ru_task set source_key=?, join_branch_num=? where id=?", 
						Arrays.asList(joinTask.getSourceKey(), joinTask.getJoinBranchNum(), joinTask.getId()));
			}
			return null; 
		}
		
		if(joinTask.getJoinBranchNum() == parentTask.getForkBranchNum())
			joinTask.setParentTaskinstId(parentTask.getParentTaskinstId());
		return joinTask;
	}
	
	/**
	 * 存储并行任务相关信息的类
	 * @author DougLei
	 */
	class ParallelTaskHolder {
		private List<Task> tasks; // 并行任务集合
		private Task joinTask; // 进行join的任务实例
		
		ParallelTaskHolder(List<Object> parentTaskinstId, boolean createJoinTaskInstance) {
			this.tasks = SessionContext.getTableSession().query(Task.class, "select * from bpm_ru_task where parent_taskinst_id=?", parentTaskinstId);
			if(createJoinTaskInstance) {
				// 设置进行join的任务实例, 如果不存在就创建出来
				for (int i = 0; i < tasks.size(); i++) {
					if(tasks.get(i).getKey().equals(currentJoinTaskMetadataEntity.getTaskMetadata().getId())) {
						joinTask = tasks.remove(i);
						joinTask.setSourceKey(previousTaskEntity.getTask().getKey());
						joinTask.setJoinBranchNum(joinTask.getJoinBranchNum()+1);
						break;
					}
				}
				if(joinTask == null) {
					joinTask = new Task(
							previousTaskEntity.getTask().getProcinstId(), 
							previousTaskEntity.getTask().getParentTaskinstId(), 
							currentDate,
							previousTaskEntity.getTask().getKey(),
							currentJoinTaskMetadataEntity.getTaskMetadata(),
							currentJoinTaskMetadataEntity.getProcessMetadata());
					joinTask.setJoinBranchNum(1);
				}
			}
		}
	}
	
	// 判断tasks中, 是否有可以到达当前网关的
	private boolean canReachGateway(List<Task> tasks) {
		Set<String> visitedTaskIds = new HashSet<String>();
		for (Task task : tasks) {
			if(canReachGateway(currentJoinTaskMetadataEntity.getProcessMetadata().getTaskMetadataEntity(task.getKey()), visitedTaskIds))
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
			if(outputFlow.getTarget().equals(currentJoinTaskMetadataEntity.getTaskMetadata().getId()))
				return true;
			if(canReachGateway(currentJoinTaskMetadataEntity.getProcessMetadata().getTaskMetadataEntity(outputFlow.getTarget()), visitedTaskIds))
				return true;
		}
		return false;
	}
}
