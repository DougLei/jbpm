package com.douglei.bpm.process.metadata.node;

import java.util.List;

import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public abstract class TaskMetadata extends ProcessNodeMetadata{
	
	protected TaskMetadata(String id, String name) {
		super(id, name);
	}
	
	/**
	 * 当前任务是否支持多Flow
	 * @return
	 */
	public abstract boolean supportMultiFlow();
	
	/**
	 * 添加Flow
	 * @param flow
	 */
	public abstract void addFlow(FlowMetadata flow);
	
	/**
	 * 获取flow集合
	 * @return
	 */
	public abstract List<FlowMetadata> getFlows();
	
	/**
	 * 获取默认flow, 可为null
	 * @return
	 */
	public abstract FlowMetadata getDefaultFlow();
	
	/**
	 * 当前任务是否支持Flow(拥有)条件表达式
	 * @return
	 */
	public abstract boolean supportFlowConditionExpr();
	
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
