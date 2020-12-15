package com.douglei.bpm.module.runtime.task;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.handler.GeneralExecuteParameter;
import com.douglei.bpm.process.handler.ProcessHandlers;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行任务服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class TaskService {
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	@Autowired
	private ProcessHandlers processExecutors;
	
	/**
	 * 完成指定id的任务
	 * @param taskId
	 * @return 
	 */
	@Transaction
	public ExecutionResult complete(int taskId) {
		Task task = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where id = ?", Arrays.asList(taskId));
		if(task == null)
			return new ExecutionResult("不存在id为["+taskId+"]的任务");
		
		ProcessMetadata processMetadata = processContainer.getProcess(task.getProcdefId());
		return processExecutors.execute(processMetadata.getTask(task.getKey()), new GeneralExecuteParameter());
	}
	
	/**
	 * 认领指定id的任务
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public ExecutionResult claim(int taskId, String userId){
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
