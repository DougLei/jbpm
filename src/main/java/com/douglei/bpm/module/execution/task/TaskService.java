package com.douglei.bpm.module.execution.task;

import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.ExecutionException;
import com.douglei.bpm.module.execution.task.command.CarbonCopyTaskCmd;
import com.douglei.bpm.module.execution.task.command.ClaimTaskCmd;
import com.douglei.bpm.module.execution.task.command.DelegateTaskCmd;
import com.douglei.bpm.module.execution.task.command.DispatchTaskCmd;
import com.douglei.bpm.module.execution.task.command.HandleTaskCmd;
import com.douglei.bpm.module.execution.task.command.KillAndJumpTaskCmd;
import com.douglei.bpm.module.execution.task.command.SuspendTaskCmd;
import com.douglei.bpm.module.execution.task.command.TransferTaskCmd;
import com.douglei.bpm.module.execution.task.command.UnclaimTaskCmd;
import com.douglei.bpm.module.execution.task.command.ViewCarbonCopyCmd;
import com.douglei.bpm.module.execution.task.command.WakeTaskCmd;
import com.douglei.bpm.module.execution.task.command.parameter.DispatchTaskParameter;
import com.douglei.bpm.module.execution.task.command.parameter.HandleTaskParameter;
import com.douglei.bpm.module.execution.task.command.parameter.KillAndJumpTaskParameter;
import com.douglei.bpm.module.execution.task.runtime.TaskEntity;
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
	public Result claim(int taskId, String userId){
		TaskEntity entity = new TaskEntity(taskId, container);
		if(entity.isActive())
			return commandExecutor.execute(new ClaimTaskCmd(entity, userId));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	/**
	 * 认领指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @return
	 */
	@Transaction
	public Result claim(String taskinstId, String userId){
		TaskEntity entity = new TaskEntity(taskinstId, container);
		if(entity.isActive())
			return commandExecutor.execute(new ClaimTaskCmd(entity, userId));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	
	/**
	 * 取消认领指定id的任务
	 * @param taskId
	 * @param userId
	 * @param strict 如果进行过抄送操作, 且抄送被查看, 取消认领时是否强制删除相关数据; 建议传入false
	 * @return
	 */
	@Transaction
	public Result unclaim(int taskId, String userId, boolean strict){
		TaskEntity entity = new TaskEntity(taskId, container);
		if(entity.isActive())
			return commandExecutor.execute(new UnclaimTaskCmd(entity, userId, strict));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	/**
	 * 取消认领指定id的任务
	 * @param taskinstId
	 * @param userId
	 * @param strict 如果进行过抄送操作, 且抄送被查看, 取消认领时是否强制删除相关数据; 建议传入false
	 * @return
	 */
	@Transaction
	public Result unclaim(String taskinstId, String userId, boolean strict){
		TaskEntity entity = new TaskEntity(taskinstId, container);
		if(entity.isActive())
			return commandExecutor.execute(new UnclaimTaskCmd(entity, userId, strict));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
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
	public Result delegate(int taskId, String userId, String assignedUserId, String reason) {
		TaskEntity entity = new TaskEntity(taskId, container);
		if(entity.isActive())
			return commandExecutor.execute(new DelegateTaskCmd(entity, userId, assignedUserId, reason));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
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
	public Result delegate(String taskinstId, String userId, String assignedUserId, String reason) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		if(entity.isActive())
			return commandExecutor.execute(new DelegateTaskCmd(entity, userId, assignedUserId, reason));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
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
	public Result transfer(int taskId, String userId, String assignedUserId, String reason) {
		TaskEntity entity = new TaskEntity(taskId, container);
		if(entity.isActive())
			return commandExecutor.execute(new TransferTaskCmd(entity, userId, assignedUserId, reason));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
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
	public Result transfer(String taskinstId, String userId, String assignedUserId, String reason) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		if(entity.isActive())
			return commandExecutor.execute(new TransferTaskCmd(entity, userId, assignedUserId, reason));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	
	/**
	 * 抄送指定id的任务
	 * @param taskId
	 * @param userId 发起抄送的用户id
	 * @param assignedUserIds 接受抄送的用户id集合
	 * @return 
	 */
	@Transaction
	public Result carbonCopy(int taskId, String userId, List<String> assignedUserIds) {
		TaskEntity entity = new TaskEntity(taskId, container);
		if(entity.isActive())
			return commandExecutor.execute(new CarbonCopyTaskCmd(entity, userId, assignedUserIds));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	/**
	 * 抄送指定id的任务
	 * @param taskinstId
	 * @param userId 发起抄送的用户id
	 * @param assignedUserIds 接受抄送的用户id集合
	 * @return 
	 */
	@Transaction
	public Result carbonCopy(String taskinstId, String userId, List<String> assignedUserIds) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		if(entity.isActive())
			return commandExecutor.execute(new CarbonCopyTaskCmd(entity, userId, assignedUserIds));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	/**
	 * 查看抄送
	 * @param taskinstId
	 * @param userId 查看抄送的用户id
	 * @return 
	 */
	@Transaction
	public Result viewCarbonCopy(String taskinstId, String userId) {
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
	public Result wake(int taskId) {
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new WakeTaskCmd(entity));
	}
	/**
	 * 唤醒指定id的任务
	 * @param taskinstId
	 * @return
	 */
	@Transaction
	public Result wake(String taskinstId) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new WakeTaskCmd(entity));
	}
	
	/**
	 * 挂起指定id的任务
	 * @param taskId
	 * @return
	 */
	@Transaction
	public Result suspend(int taskId) {
		TaskEntity entity = new TaskEntity(taskId, container);
		return commandExecutor.execute(new SuspendTaskCmd(entity));
	}
	/**
	 * 挂起指定id的任务
	 * @param taskinstId
	 * @return
	 */
	@Transaction
	public Result suspend(String taskinstId) {
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
	public Result handle(int taskId, HandleTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskId, container);
		if(entity.isActive())
			return commandExecutor.execute(new HandleTaskCmd(entity, parameter));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	/**
	 * 办理指定id的任务
	 * @param taskinstId
	 * @param parameter
	 * @return 返回对象的success=true时, 其Object属性为boolean类型, 标识当前用户是否可以进行调度操作
	 */
	@Transaction
	public Result handle(String taskinstId, HandleTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		if(entity.isActive())
			return commandExecutor.execute(new HandleTaskCmd(entity, parameter));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	
	/**
	 * 调度指定id的任务
	 * @param taskId
	 * @param parameter
	 * @return
	 */
	@Transaction
	public Result dispatch(int taskId, DispatchTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskId, container);
		if(entity.isActive())
			return commandExecutor.execute(new DispatchTaskCmd(entity, parameter));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	/**
	 * 调度指定id的任务
	 * @param taskinstId
	 * @param parameter
	 * @return
	 */
	@Transaction
	public Result dispatch(String taskinstId, DispatchTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		if(entity.isActive())
			return commandExecutor.execute(new DispatchTaskCmd(entity, parameter));
		throw new ExecutionException("操作失败, ["+entity.getName()+"]任务处于挂起状态");
	}
	
	/**
	 * 终止指定id的任务并跳转
	 * @param taskId
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public Result killAndJump(int taskId, KillAndJumpTaskParameter parameter) {
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
	public Result killAndJump(String taskinstId, KillAndJumpTaskParameter parameter) {
		TaskEntity entity = new TaskEntity(taskinstId, container);
		return commandExecutor.execute(new KillAndJumpTaskCmd(entity, parameter));
	}
}
