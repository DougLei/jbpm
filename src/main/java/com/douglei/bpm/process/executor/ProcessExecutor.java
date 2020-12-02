package com.douglei.bpm.process.executor;

import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.component.ExecutionResult;
import com.douglei.bpm.process.metadata.node.ProcessNodeMetadata;
import com.douglei.bpm.process.metadata.node.TaskNodeMetadata;

/**
 * 
 * @author DougLei
 */
@SuppressWarnings("rawtypes")
@MultiInstance
public abstract class ProcessExecutor<M extends ProcessNodeMetadata, EM extends ProcessExecutionParameter> {
	
	@Autowired
	protected ProcessExecutors executors;
	
	/**
	 * 获取元数据的类型
	 * @return
	 */
	protected abstract Class<M> getMetadataClass();
	
	/**
	 * 执行
	 * @param metadata
	 * @param parameter
	 * @return 
	 */
	public abstract ExecutionResult execute(M metadata, EM parameter);

	/**
	 * 执行flow
	 * @param metadata
	 * @param variableMap
	 */
	protected final void executeFlow(TaskNodeMetadata metadata, Map<String, Object> variableMap) {
		// TODO 传入当前任务的所有变量进行流转判断
		// 
		/*
		 * 迭代flow集合, 找到第一条符合条件的进入对应的任务即可; 如果都没匹配, 则找寻default=true的那条flow; 
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		
	}
}
