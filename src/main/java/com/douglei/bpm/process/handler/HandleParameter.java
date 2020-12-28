package com.douglei.bpm.process.handler;

import java.util.Date;

import com.douglei.bpm.module.runtime.task.Task;

/**
 * 办理参数接口
 * @author DougLei
 */
public interface HandleParameter {
	
	/**
	 * 获取当前时间
	 * @return
	 */
	Date getCurrentDate();
	
	/**
	 * 获取流程实体
	 * @return
	 */
	ProcessEntity getProcessEntity() ;
	
	/**
	 * 添加办理的任务实例
	 * @param task
	 */
	void addTask(Task task);
	
	/**
	 * 获取(最新)办理的任务实例
	 * @return
	 */
	Task getTask();
	
	/**
	 * 获取办理的用户实体
	 * @return
	 */
	UserEntity getUserEntity();
	
	/**
	 * 获取办理时流程变量实体
	 * @return
	 */
	VariableEntities getVariableEntities();
}
