package com.douglei.bpm.module.runtime.instance;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.command.StartProcessCmd;
import com.douglei.bpm.module.runtime.instance.command.SuspendProcessCmd;
import com.douglei.bpm.module.runtime.instance.command.TerminateProcessCmd;
import com.douglei.bpm.module.runtime.instance.command.WakeProcessCmd;
import com.douglei.bpm.module.runtime.instance.command.parameter.StartParameter;
import com.douglei.orm.context.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessInstanceService {
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	/**
	 * 唤醒指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public ExecutionResult wake(int id) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(id);
		return commandExecutor.execute(new WakeProcessCmd(entity));
	}
	/**
	 * 唤醒指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult wake(String procinstId) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new WakeProcessCmd(entity));
	}
	
	/**
	 * 挂起指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public ExecutionResult suspend(int id) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(id);
		return commandExecutor.execute(new SuspendProcessCmd(entity));
	}
	/**
	 * 挂起指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult suspend(String procinstId) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new SuspendProcessCmd(entity));
	}
	
	/**
	 * 终止指定id的流程实例
	 * @param id
	 * @param userId
	 * @param reason
	 * @return
	 */
	@Transaction
	public ExecutionResult terminate(int id, String userId, String reason) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(id);
		return commandExecutor.execute(new TerminateProcessCmd(entity, userId, reason));
	}
	/**
	 * 终止指定id的流程实例
	 * @param procinstId
	 * @param userId
	 * @param reason
	 * @return
	 */
	@Transaction
	public ExecutionResult terminate(String procinstId, String userId, String reason) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new TerminateProcessCmd(entity, userId, reason));
	}
	
	/**
	 * 删除指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public ExecutionResult delete(int id) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	/**
	 * 删除指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult delete(String procinstId) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 撤销删除指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public ExecutionResult undoDelete(int id) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	/**
	 * 撤销删除指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult undoDelete(String procinstId) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 物理删除指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public ExecutionResult delete4Physical(int id) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	/**
	 * 物理删除指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult delete4Physical(String procinstId) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 重置指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public ExecutionResult reset(int id) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	/**
	 * 重置指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult reset(String procinstId) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 恢复指定id的流程实例(从终止状态到运行状态)
	 * @param id
	 * @return
	 */
	@Transaction
	public ExecutionResult recovery(int id) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	/**
	 * 恢复指定id的流程实例(从终止状态到运行状态)
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult recovery(String procinstId) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	} 
	
	/**
	 * 激活指定id的流程实例(从完成状态到运行状态)
	 * @param id
	 * @return
	 */
	@Transaction
	public ExecutionResult activate(int id) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	/**
	 * 激活指定id的流程实例(从完成状态到运行状态)
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public ExecutionResult activate(String procinstId) {
		// TODO 
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 处理指定id的流程定义, 相关的所有运行实例
	 * @param procdefId
	 * @param policy 对实例的处理策略
	 */
	@Transaction
	public void handle(int procdefId, HandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
	}
	
	/**
	 * 启动流程
	 * @param parameter
	 * @return 返回对象的success=true时, 其Object属性为 {@link ProcessInstance} 类型, 记录启动的流程实例
	 */
	@Transaction
	public ExecutionResult start(StartParameter parameter) {
		return commandExecutor.execute(new StartProcessCmd(parameter));
	}
}
