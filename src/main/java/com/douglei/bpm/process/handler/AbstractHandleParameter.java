package com.douglei.bpm.process.handler;

import java.util.Date;

import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;

/**
 * 办理参数接口
 * @author DougLei
 */
public abstract class AbstractHandleParameter {
	private Date currentDate = new Date();
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public Date getCurrentDate() {
		return currentDate;
	}
	/**
	 * 获取流程实例id
	 * @return
	 */
	public abstract String getProcessInstanceId();
	/**
	 * 获取本次办理关联的业务id
	 * @return
	 */
	public abstract String getBusinessId();
	/**
	 * 获取流程元数据实例
	 * @return
	 */
	public abstract ProcessMetadata getProcessMetadata();
	
	/**
	 * 获取任务实体处理器
	 * @return
	 */
	public abstract TaskEntityHandler getTaskEntityHandler();
	/**
	 * 获取办理的用户实体
	 * @return
	 */
	public abstract UserEntity getUserEntity();
	/**
	 * 获取办理时流程变量实体
	 * @return
	 */
	public abstract VariableEntities getVariableEntities();
}
