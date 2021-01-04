package com.douglei.bpm.process.handler;

import java.util.Date;

import com.douglei.bpm.process.metadata.ProcessMetadata;

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
	 * 获取流程实例id
	 * @return
	 */
	String getProcessInstanceId();
	
	/**
	 * 获取流程元数据实例
	 * @return
	 */
	ProcessMetadata getProcessMetadata();
	
	/**
	 * 添加一个新办理的任务实例, 作为当前任务
	 * @param taskEntity
	 */
	void addTaskEntity(TaskEntity taskEntity);
	/**
	 * 获取上一个办理的任务实例
	 * <p>
	 * 当只有一个任务实例时, getPreviousTaskEntity() == getCurrentTaskEntity()
	 * @return
	 */
	TaskEntity getPreviousTaskEntity();
	/**
	 * 获取当前办理的任务实例
	 * @return
	 */
	TaskEntity getCurrentTaskEntity();
	
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
