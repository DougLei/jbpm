package com.douglei.bpm.process.executor;

import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.component.ExecutionResult;
import com.douglei.bpm.process.NodeType;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;
import com.douglei.bpm.process.metadata.node.TaskNodeMetadata;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
@MultiInstance
public abstract class Executor<M extends ProcessNodeMetadata, EM extends ProcessExecutionParameter> {
	
	@Autowired
	protected ProcessExecutors executors;
	
	/**
	 * 执行flow
	 * @param metadata
	 * @param variableMap
	 * @return 
	 */
	protected final boolean executeFlow(TaskNodeMetadata metadata, Map<String, Object> variableMap) {
		// TODO 传入当前任务的所有变量进行流转判断
		// 
		/*
		 * 迭代flow集合, 找到第一条符合条件的进入对应的任务即可; 如果都没匹配, 则抛出异常
		 * 
		 * 
		 * 
		 * 
		 */
		
		for(FlowMetadata flow : metadata.getFlows()) {
			if(executors.execute(flow, null).isSuccess())
				return true;
		}
		return false;
	}
	
	/**
	 * 获取执行器类型
	 * @return
	 */
	protected abstract NodeType getType();
	
	/**
	 * 执行
	 * @param metadata
	 * @param parameter
	 * @return 
	 */
	public abstract ExecutionResult<?> execute(M metadata, EM parameter);
}
