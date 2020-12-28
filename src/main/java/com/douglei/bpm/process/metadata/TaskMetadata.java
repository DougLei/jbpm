package com.douglei.bpm.process.metadata;

/**
 * 
 * @author DougLei
 */
public abstract class TaskMetadata extends ProcessNodeMetadata{
	private String defaultFlowId;
	
	protected TaskMetadata(String id, String name) {
		this(id, name, null);
	}
	protected TaskMetadata(String id, String name, String defaultFlowId) {
		super(id, name);
		this.defaultFlowId = defaultFlowId;
	}
	
	/**
	 * 获取默认flow的id, 可为null
	 * @return
	 */
	public final String getDefaultFlowId() {
		return defaultFlowId;
	}
	
	/**
	 * 获取pageID, 可为null
	 * @return
	 */
	public String getPageID() {
		return null;
	}
	
	/**
	 * 当前任务是否支持用户处理
	 * @return
	 */
	public boolean supportUserHandling() {
		return false;
	}
}
