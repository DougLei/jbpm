package com.douglei.bpm.process.metadata;

/**
 * 
 * @author DougLei
 */
public abstract class TaskMetadata extends ProcessNodeMetadata{
	
	protected TaskMetadata(String id, String name) {
		super(id, name);
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
