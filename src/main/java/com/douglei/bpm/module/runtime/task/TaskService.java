package com.douglei.bpm.module.runtime.task;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行任务服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class TaskService {
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	/**
	 * 完成指定id的任务
	 * @param taskId
	 * @return 
	 */
	@Transaction
	public ExecutionResult<Integer> complete(int taskId) {
		return commandExecutor.execute(new CompleteTaskCommand(taskId));
	}
	
	
}
