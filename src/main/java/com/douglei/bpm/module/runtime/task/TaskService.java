package com.douglei.bpm.module.runtime.task;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.command.ClaimTaskCmd;
import com.douglei.bpm.module.runtime.task.command.HandleTaskCmd;
import com.douglei.bpm.module.runtime.task.command.UnclaimTaskCmd;
import com.douglei.bpm.process.api.container.ProcessContainerProxy;
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
		TaskInstance taskInstance = new TaskInstance(taskId, processContainer);
		return commandExecutor.execute(new ClaimTaskCmd(taskInstance, userId));
	}
	/**
	 * 认领指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @return
	 */
	@Transaction
	public ExecutionResult claim(String taskinstId, String userId){
		TaskInstance taskInstance = new TaskInstance(taskinstId, processContainer);
		return commandExecutor.execute(new ClaimTaskCmd(taskInstance, userId));
	}
	
	/**
	 * 取消认领指定id的任务
	 * @param taskId
	 * @param userId
	 * @return
	 */
	@Transaction
	public ExecutionResult unclaim(int taskId, String userId){
		TaskInstance taskInstance = new TaskInstance(taskId, processContainer);
		return commandExecutor.execute(new UnclaimTaskCmd(taskInstance, userId));
	}
	/**
	 * 取消认领指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @return
	 */
	@Transaction
	public ExecutionResult unclaim(String taskinstId, String userId){
		TaskInstance taskInstance = new TaskInstance(taskinstId, processContainer);
		return commandExecutor.execute(new UnclaimTaskCmd(taskInstance, userId));
	}
	
	/**
	 * 办理指定id的任务
	 * @param taskId
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult handle(int taskId, TaskHandleParameter parameter) {
		TaskInstance taskInstance = new TaskInstance(taskId, processContainer);
		return commandExecutor.execute(new HandleTaskCmd(taskInstance, parameter));
	}
	/**
	 * 办理指定id的任务
	 * @param taskinstId
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult handle(String taskinstId, TaskHandleParameter parameter) {
		TaskInstance taskInstance = new TaskInstance(taskinstId, processContainer);
		return commandExecutor.execute(new HandleTaskCmd(taskInstance, parameter));
	}
	
	// TODO 还未实现的方法
	// 抄送
	// 委托
	// 转办
	// 协办
}
