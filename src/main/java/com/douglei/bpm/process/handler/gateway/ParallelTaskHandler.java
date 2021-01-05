package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.process.handler.TaskEntity;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;

/**
 * 并行任务处理器
 * @author DougLei
 */
public class ParallelTaskHandler {

	public ParallelTaskHandler(TaskEntity previousTaskEntity, TaskMetadataEntity<? extends TaskMetadata> currentTaskMetadataEntity) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 进行合并操作
	 * @return 是否完成合并
	 */
	public synchronized boolean join() {
//		String parentTaskinstId = handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getParentTaskinstId();
//		if(parentTaskinstId == null) {
//			if(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().isCreateBranch()) // 如果上一个任务会创建分支, 则这里要记录上一个任务的实例id, 作为当前任务的父任务, 例如两个并行网关连在一起
//				this.parentTaskinstId = handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getTaskinstId();
//			return true;
//		}
		
		
		
		return false;
	}

	/**
	 * 获取当前任务关联的父任务实例id值
	 * @return
	 */
	public String getCurrentTaskParentTaskinstId() {
		// TODO Auto-generated method stub
		return null;
	}

	

	/*
	 * 启动了几个并行任务
	 * 已经完成了几个任务
	 * 还剩几个任务未完成
	 * 
	 * 这些任务中要忽略一些特殊的任务, 例如网关任务
	 * 
	 * 
	 */
	
	
	
//	select 'x' b from sys_user
	
	
	
	
}
