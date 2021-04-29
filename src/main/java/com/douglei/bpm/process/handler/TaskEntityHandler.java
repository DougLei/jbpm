package com.douglei.bpm.process.handler;

import java.util.LinkedList;

/**
 * 
 * @author DougLei
 */
public class TaskEntityHandler {
	private TaskEntity currentTaskEntity; // 当前办理的任务实体实例
	private LinkedList<TaskEntity> historyTaskEntities; // 历史办理的任务实体实例集合
	
	/**
	 * 设置当前办理的任务实体实例
	 * @param taskEntity
	 */
	public void setCurrentTaskEntity(TaskEntity taskEntity) {
		this.currentTaskEntity = taskEntity;
	}
	
	/**
	 * 获取当前办理的任务实体实例
	 * <p>
	 * 在没有设置当前办理的任务实体实例时, 该方法会返回null
	 * @return
	 */
	public TaskEntity getCurrentTaskEntity() {
		return currentTaskEntity;
	}
	
	/**
	 * 获取上一个办理的任务的key
	 * <p>
	 * 作为当前任务的sourceKey
	 * @return
	 */
	public String getPreviousTaskKey() {
		TaskEntity previousTask = getPreviousTaskEntity();
		if(previousTask == null)
			return null;
		return previousTask.getTask().getKey();
	}
	
	/**
	 * 获取上一个办理的任务实例
	 * <p>
	 * 没有任何历史任务时, 返回null
	 * @return
	 */
	public TaskEntity getPreviousTaskEntity() {
		if(historyTaskEntities == null)
			return null;
		return historyTaskEntities.getLast();
	}
	
	/**
	 * 获取历史办理的任务实体实例集合
	 * <p>
	 * 没有任何历史任务时, 返回null
	 * @return
	 */
	public LinkedList<TaskEntity> getHistoryTaskEntities() {
		return historyTaskEntities;
	}

	/**
	 * 任务数据调度
	 * <p>
	 * 将当前办理的任务实体实例, 转移到历史任务实体实例集合中, 并置空当前办理任务
	 */
	public void dispatch() {
		if(historyTaskEntities == null)
			historyTaskEntities = new LinkedList<TaskEntity>();
		historyTaskEntities.add(currentTaskEntity);
		this.currentTaskEntity = null;
	}
}
