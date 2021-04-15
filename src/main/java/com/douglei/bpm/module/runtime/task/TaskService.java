package com.douglei.bpm.module.runtime.task;

import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.command.WakeTaskCmd;
import com.douglei.bpm.module.runtime.task.command.CarbonCopyTaskCmd;
import com.douglei.bpm.module.runtime.task.command.ClaimTaskCmd;
import com.douglei.bpm.module.runtime.task.command.DelegateTaskCmd;
import com.douglei.bpm.module.runtime.task.command.DispatchTaskCmd;
import com.douglei.bpm.module.runtime.task.command.HandleTaskCmd;
import com.douglei.bpm.module.runtime.task.command.KillAndJumpTaskCmd;
import com.douglei.bpm.module.runtime.task.command.SuspendTaskCmd;
import com.douglei.bpm.module.runtime.task.command.TransferTaskCmd;
import com.douglei.bpm.module.runtime.task.command.UnclaimTaskCmd;
import com.douglei.bpm.module.runtime.task.command.ViewCarbonCopyCmd;
import com.douglei.bpm.module.runtime.task.command.parameter.DispatchTaskParameter;
import com.douglei.bpm.module.runtime.task.command.parameter.HandleTaskParameter;
import com.douglei.bpm.module.runtime.task.command.parameter.KillAndJumpTaskParameter;
import com.douglei.bpm.process.mapping.ProcessMappingContainer;
import com.douglei.orm.context.Transaction;

/**
 * 运行任务服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class TaskService {
	
	@Autowired
	private ProcessMappingContainer container;
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	// ------------------------------------------------------------------------
	// 针对用户任务的api
	// ------------------------------------------------------------------------
	/**
	 * 认领指定id的任务
	 * @param taskId
	 * @param userId
	 * @return
	 */
	@Transaction
	public ExecutionResult claim(int taskId, String userId){
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new ClaimTaskCmd(entity, userId));
	}
	/**
	 * 认领指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @return
	 */
	@Transaction
	public ExecutionResult claim(String taskinstId, String userId){
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new ClaimTaskCmd(entity, userId));
	}
	
	/**
	 * 取消认领指定id的任务
	 * @param taskId
	 * @param userId
	 * @param strict 如果进行过抄送操作, 且抄送被查看, 取消认领时是否强制删除相关数据; 建议传入false
	 * @return
	 */
	@Transaction
	public ExecutionResult unclaim(int taskId, String userId, boolean strict){
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new UnclaimTaskCmd(entity, userId, strict));
	}
	/**
	 * 取消认领指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @param strict 如果进行过抄送操作, 且抄送被查看, 取消认领时是否强制删除相关数据; 建议传入false
	 * @return
	 */
	@Transaction
	public ExecutionResult unclaim(String taskinstId, String userId, boolean strict){
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new UnclaimTaskCmd(entity, userId, strict));
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
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new DelegateTaskCmd(entity, userId, assignedUserId, reason));
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
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new DelegateTaskCmd(entity, userId, assignedUserId, reason));
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
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new TransferTaskCmd(entity, userId, assignedUserId, reason));
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
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new TransferTaskCmd(entity, userId, assignedUserId, reason));
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
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new CarbonCopyTaskCmd(entity, userId, assignedUserIds));
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
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new CarbonCopyTaskCmd(entity, userId, assignedUserIds));
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
	
	// ------------------------------------------------------------------------
	// 针对所有任务的api
	// ------------------------------------------------------------------------
	/**
	 * 唤醒指定id的任务
	 * @param taskId
	 * @return
	 */
	@Transaction
	public ExecutionResult wake(int taskId) {
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new WakeTaskCmd(entity));
	}
	/**
	 * 唤醒指定id的任务
	 * @param taskinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult wake(String taskinstId) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new WakeTaskCmd(entity));
	}
	
	/**
	 * 挂起指定id的任务
	 * @param taskId
	 * @return
	 */
	@Transaction
	public ExecutionResult suspend(int taskId) {
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new SuspendTaskCmd(entity));
	}
	/**
	 * 挂起指定id的任务
	 * @param taskinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult suspend(String taskinstId) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new SuspendTaskCmd(entity));
	}
	
	/**
	 * 办理指定id的任务
	 * @param taskId
	 * @param parameter
	 * @return 返回对象的success=true时, 其Object属性为boolean类型, 标识当前用户是否可以进行调度操作
	 */
	@Transaction
	public ExecutionResult handle(int taskId, HandleTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new HandleTaskCmd(entity, parameter));
	}
	/**
	 * 办理指定id的任务
	 * @param taskinstId
	 * @param parameter
	 * @return 返回对象的success=true时, 其Object属性为boolean类型, 标识当前用户是否可以进行调度操作
	 */
	@Transaction
	public ExecutionResult handle(String taskinstId, HandleTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new HandleTaskCmd(entity, parameter));
	}
	
	/**
	 * 调度指定id的任务
	 * @param taskId
	 * @param parameter
	 * @return
	 */
	@Transaction
	public ExecutionResult dispatch(int taskId, DispatchTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new DispatchTaskCmd(entity, parameter));
	}
	/**
	 * 调度指定id的任务
	 * @param taskinstId
	 * @param parameter
	 * @return
	 */
	@Transaction
	public ExecutionResult dispatch(String taskinstId, DispatchTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new DispatchTaskCmd(entity, parameter));
	}
	
	/**
	 * 终止指定id的任务并跳转
	 * @param taskId
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult killAndJump(int taskId, KillAndJumpTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new KillAndJumpTaskCmd(entity, parameter));
	}
	/**
	 * 终止指定id的任务并跳转
	 * @param taskinstId
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult killAndJump(String taskinstId, KillAndJumpTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new KillAndJumpTaskCmd(entity, parameter));
	}
}
