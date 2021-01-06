package com.douglei.bpm.process.metadata;

/**
 * 
 * @author DougLei
 */
public abstract class TaskMetadata extends ProcessNodeMetadata{
	private String defaultOutputFlowId;
	
	protected TaskMetadata(String id, String name, String defaultOutputFlowId) {
		super(id, name);
		this.defaultOutputFlowId = defaultOutputFlowId;
	}
	
	/**
	 * 获取任务关联的pageID, 默认值为null
	 * @return
	 */
	public String getPageID() {
		return null;
	}
	/**
	 * 获取任务默认输出流的id, 默认值为null
	 * @return
	 */
	public final String getDefaultOutputFlowId() {
		return defaultOutputFlowId;
	}
	/**
	 * 任务是否需要用户办理, 默认值为false
	 * @return
	 */
	public boolean requiredUserHandle() {
		return false;
	}
}
