package com.douglei.bpm.module.runtime.task;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.command.ClaimTaskCmd;
import com.douglei.bpm.module.runtime.task.command.CompleteTaskCmd;
import com.douglei.bpm.process.container.ProcessContainerProxy;
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
	private CommandExecutor commandExecutor;
	
	/**
	 * 认领指定id的任务
	 * @param taskId
	 * @param userId
	 * @return
	 */
	@Transaction
	public ExecutionResult claim(int taskId, String userId){
		TaskEntity taskEntity = new TaskEntity(taskId, processContainer);
		return commandExecutor.execute(new ClaimTaskCmd(taskEntity, userId));
	}
	/**
	 * 认领指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @return
	 */
	@Transaction
	public ExecutionResult claim(String taskinstId, String userId){
		TaskEntity taskEntity = new TaskEntity(taskinstId, processContainer);
		return commandExecutor.execute(new ClaimTaskCmd(taskEntity, userId));
	}
	
	/**
	 * 完成指定id的任务
	 * @param taskId
	 * @param userId
	 * @param assigneeUserIds 指派的用户ids
	 * @return 
	 */
	@Transaction
	public ExecutionResult complete(int taskId, String userId, String... assigneeUserIds) {
		TaskEntity taskEntity = new TaskEntity(taskId, processContainer);
		return commandExecutor.execute(new CompleteTaskCmd(taskEntity, userId, assigneeUserIds));
	}
	/**
	 * 完成指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @param assigneeUserIds 指派的用户ids
	 * @return 
	 */
	@Transaction
	public ExecutionResult complete(String taskinstId, String userId, String... assigneeUserIds) {
		TaskEntity taskEntity = new TaskEntity(taskinstId, processContainer);
		return commandExecutor.execute(new CompleteTaskCmd(taskEntity, userId, assigneeUserIds));
	}
}
