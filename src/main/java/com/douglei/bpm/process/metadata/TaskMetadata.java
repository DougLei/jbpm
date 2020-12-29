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
	public String getDefaultOutputFlowId() {
		return null;
	}
	
	/**
	 * 任务是否需要用户办理, 默认值为false
	 * @return
	 */
	public boolean requiredUserHandle() {
		return false;
	}
	/**
	 * 任务是否需要其关联的输入流集合, 默认值为false
	 * @return
	 */
	public boolean requiredInputFlows() {
		return false;
	}
	/**
	 * 任务是否需要其关联的输出流集合, 默认值为false
	 * @return
	 */
	public boolean requiredOutputFlows() {
		return false;
	}
}
