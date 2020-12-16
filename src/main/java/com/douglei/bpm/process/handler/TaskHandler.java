package com.douglei.bpm.process.handler;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 
 * @author DougLei
 */
public interface TaskHandler<M extends TaskMetadata, S extends ExecuteParameter, P extends ExecuteParameter> {
	
	/**
	 * 启动任务
	 * @param taskMetadata
	 * @param executeParameter
	 * @return
	 */
	ExecutionResult startup(M taskMetadata, S executeParameter);
	
	/**
	 * 执行任务
	 * @param taskMetadata
	 * @param executeParameter
	 * @return
	 */
	ExecutionResult execute(M taskMetadata, P executeParameter);

	/**
	 * 获取类型
	 * @return
	 */
	Type getType();
}
