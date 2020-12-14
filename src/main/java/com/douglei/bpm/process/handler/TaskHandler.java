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
public abstract class TaskHandler<M extends TaskMetadata, EM extends ExecuteParameter> {
	
	@Autowired
	protected TaskDispatcher taskDispatcher;
	
	/**
	 * 启动任务
	 * @param taskMetadata
	 * @param executeParameter
	 * @return
	 */
	public abstract ExecutionResult startup(M taskMetadata, EM executeParameter);
	
	/**
	 * 执行任务
	 * @param taskMetadata
	 * @param executeParameter
	 * @return
	 */
	public abstract ExecutionResult execute(M taskMetadata, EM executeParameter);

	/**
	 * 获取任务类型
	 * @return
	 */
	public abstract Type getType();
}
