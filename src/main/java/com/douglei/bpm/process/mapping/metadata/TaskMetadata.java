package com.douglei.bpm.process.mapping.metadata;

import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public abstract class TaskMetadata extends ProcessNodeMetadata{
	private String defaultOutputFlowId;
	
	protected TaskMetadata(String id, String name, String defaultOutputFlowId) {
		super(id, name);
		this.defaultOutputFlowId = StringUtil.isEmpty(defaultOutputFlowId)?null:defaultOutputFlowId;
	}
	
	/**
	 * 当前任务是否支持输入流, 默认值为true
	 * @return
	 */
	public boolean supportInputFlows() {
		return true;
	}
	/**
	 * 当前任务是否支持输出流, 默认值为true
	 * @return
	 */
	public boolean supportOutFlows() {
		return true;
	}
	
	/**
	 * 获取任务默认输出流的id, 默认值为null
	 * @return
	 */
	public final String getDefaultOutputFlowId() {
		return defaultOutputFlowId;
	}
	/**
	 * 获取任务关联的pageID, 默认值为null
	 * @param metadata
	 * @return
	 */
	public String getPageID(ProcessMetadata metadata) {
		return null;
	}
	/**
	 * 获取任务的办理时限, 默认值为null
	 * @return
	 */
	public TimeLimit getTimeLimit() {
		return null;
	}
	/**
	 * 是否是用户任务, 默认值为false
	 * @return
	 */
	public boolean isUserTask() {
		return false;
	}
}
