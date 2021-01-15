package com.douglei.bpm.module.runtime.task;

import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.command.CarbonCopyTaskCmd;
import com.douglei.bpm.module.runtime.task.command.ClaimTaskCmd;
import com.douglei.bpm.module.runtime.task.command.DelegateTaskCmd;
import com.douglei.bpm.module.runtime.task.command.HandleTaskCmd;
import com.douglei.bpm.module.runtime.task.command.TransferTaskCmd;
import com.douglei.bpm.module.runtime.task.command.UnclaimTaskCmd;
import com.douglei.bpm.module.runtime.task.command.ViewCarbonCopyCmd;
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
	 * @param strict 是否强制删除任务相关的其他数据信息, 目前主要是抄送信息; 建议默认传入false
	 * @return
	 */
	@Transaction
	public ExecutionResult unclaim(int taskId, String userId, boolean strict){
		TaskInstance taskInstance = new TaskInstance(taskId, processContainer);
		return commandExecutor.execute(new UnclaimTaskCmd(taskInstance, userId, strict));
	}
	/**
	 * 取消认领指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @param strict 是否强制删除任务相关的其他数据信息, 目前主要是抄送信息; 建议默认传入false
	 * @return
	 */
	@Transaction
	public ExecutionResult unclaim(String taskinstId, String userId, boolean strict){
		TaskInstance taskInstance = new TaskInstance(taskinstId, processContainer);
		return commandExecutor.execute(new UnclaimTaskCmd(taskInstance, userId, strict));
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
	
	/**
	 * 委托指定id的任务
	 * @param taskId
	 * @param userId 发起委托的用户id
	 * @param assignedUserId 接受委托的用户id
	 * @param reason 原因
	 * @return 
	 */
	@Transaction
	public ExecutionResult delegate(int taskId, String userId, String assignedUserId, String reason) {
		TaskInstance taskInstance = new TaskInstance(taskId, processContainer);
		return commandExecutor.execute(new DelegateTaskCmd(taskInstance, userId, assignedUserId, reason));
	}
	/**
	 * 委托指定id的任务
	 * @param taskinstId
	 * @param userId 发起委托的用户id
	 * @param assignedUserId 接受委托的用户id
	 * @param reason 原因
	 * @return 
	 */
	@Transaction
	public ExecutionResult delegate(String taskinstId, String userId, String assignedUserId, String reason) {
		TaskInstance taskInstance = new TaskInstance(taskinstId, processContainer);
		return commandExecutor.execute(new DelegateTaskCmd(taskInstance, userId, assignedUserId, reason));
	}
	
	/**
	 * 转办指定id的任务
	 * @param taskId
	 * @param userId 发起转办的用户id
	 * @param assignedUserId 接受转办的用户id
	 * @param reason 原因
	 * @return 
	 */
	@Transaction
	public ExecutionResult transfer(int taskId, String userId, String assignedUserId, String reason) {
		TaskInstance taskInstance = new TaskInstance(taskId, processContainer);
		return commandExecutor.execute(new TransferTaskCmd(taskInstance, userId, assignedUserId, reason));
	}
	/**
	 * 转办指定id的任务
	 * @param taskinstId
	 * @param userId 发起转办的用户id
	 * @param assignedUserId 接受转办的用户id
	 * @param reason 原因
	 * @return 
	 */
	@Transaction
	public ExecutionResult transfer(String taskinstId, String userId, String assignedUserId, String reason) {
		TaskInstance taskInstance = new TaskInstance(taskinstId, processContainer);
		return commandExecutor.execute(new TransferTaskCmd(taskInstance, userId, assignedUserId, reason));
	}
	
	/**
	 * 抄送指定id的任务
	 * @param taskId
	 * @param userId 发起抄送的用户id
	 * @param assignedUserIds 接受抄送的用户id集合
	 * @return 
	 */
	@Transaction
	public ExecutionResult carbonCopy(int taskId, String userId, List<String> assignedUserIds) {
		TaskInstance taskInstance = new TaskInstance(taskId, processContainer);
		return commandExecutor.execute(new CarbonCopyTaskCmd(taskInstance, userId, assignedUserIds));
	}
	/**
	 * 抄送指定id的任务
	 * @param taskinstId
	 * @param userId 发起抄送的用户id
	 * @param assignedUserIds 接受抄送的用户id集合
	 * @return 
	 */
	@Transaction
	public ExecutionResult carbonCopy(String taskinstId, String userId, List<String> assignedUserIds) {
		TaskInstance taskInstance = new TaskInstance(taskinstId, processContainer);
		return commandExecutor.execute(new CarbonCopyTaskCmd(taskInstance, userId, assignedUserIds));
	}
	
	/**
	 * 查看抄送
	 * @param taskinstId
	 * @param userId 查看抄送的用户id
	 * @return 
	 */
	@Transaction
	public ExecutionResult viewCarbonCopy(String taskinstId, String userId) {
		return commandExecutor.execute(new ViewCarbonCopyCmd(taskinstId, userId));
	}
}
