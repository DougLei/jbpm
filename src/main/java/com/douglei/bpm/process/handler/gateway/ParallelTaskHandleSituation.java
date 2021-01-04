package com.douglei.bpm.process.handler.gateway;

import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.gateway.AbstractGatewayMetadata;

/**
 * 并行任务办理情况
 * @author DougLei
 */
public class ParallelTaskHandleSituation {

	public ParallelTaskHandleSituation(String parentTaskinstId) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 办理情况是否是无效
	 * @return
	 */
	public boolean invalid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 是否完成所有并行任务
	 * @return
	 */
	public boolean isAllCompleted() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 获取父并行网关任务实例
	 * @return
	 */
	public Task getParentParallelGatewayTask() {
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
