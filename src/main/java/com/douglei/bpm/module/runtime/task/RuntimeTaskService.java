package com.douglei.bpm.module.runtime.task;

import com.douglei.bpm.bean.Autowire;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.process.ProcessHandler;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行任务服务
 * @author DougLei
 */
@Bean
public class RuntimeTaskService {
	
	@Autowire
	private ProcessHandler processHandler;
	
	/**
	 * 完成指定id的任务
	 * @param taskId
	 * @return  返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult complete(int taskId) {
		// TODO 
		
		return null;
	}
}
