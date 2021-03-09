package com.douglei.bpm.process.handler;

import java.util.Date;

import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;

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
	 * 获取本次办理关联的业务id
	 * @return
	 */
	String getBusinessId();
	/**
	 * 获取流程元数据实例
	 * @return
	 */
	ProcessMetadata getProcessMetadata();
	
	/**
	 * 获取任务实体处理器
	 * @return
	 */
	TaskEntityHandler getTaskEntityHandler();
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
