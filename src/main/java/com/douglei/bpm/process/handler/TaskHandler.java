package com.douglei.bpm.process.handler;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.MultiInstance;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.Type;
import com.douglei.bpm.process.metadata.node.TaskMetadata;

/**
 * 任务处理器
 * @author DougLei
 */
@MultiInstance
public abstract class TaskHandler<M extends TaskMetadata, P extends ExecuteParameter> {
	
	@Autowired
	protected TaskScheduler taskScheduler;
	
	/**
	 * 启动任务
	 * @param taskMetadata
	 * @param executeParameter
	 * @return
	 */
	public abstract ExecutionResult startup(M taskMetadata, P executeParameter);
	
	/**
	 * 执行任务
	 * @param taskMetadata
	 * @param executeParameter
	 * @return
	 */
	public abstract ExecutionResult execute(M taskMetadata, P executeParameter);

	/**
	 * 获取类型
	 * @return
	 */
	public abstract Type getType();
}
