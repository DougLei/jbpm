package com.douglei.bpm.module.execution.instance;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.command.ActivateProcessCmd;
import com.douglei.bpm.module.execution.instance.command.Delete4PhysicalProcessCmd;
import com.douglei.bpm.module.execution.instance.command.DeleteProcessCmd;
import com.douglei.bpm.module.execution.instance.command.RecoveryProcessCmd;
import com.douglei.bpm.module.execution.instance.command.StartProcessCmd;
import com.douglei.bpm.module.execution.instance.command.SuspendProcessCmd;
import com.douglei.bpm.module.execution.instance.command.TerminateProcessCmd;
import com.douglei.bpm.module.execution.instance.command.UndoDeleteProcessCmd;
import com.douglei.bpm.module.execution.instance.command.WakeProcessCmd;
import com.douglei.bpm.module.execution.instance.command.parameter.ActivateParameter;
import com.douglei.bpm.module.execution.instance.command.parameter.StartParameter;
import com.douglei.bpm.module.execution.instance.history.HistoryProcessInstanceEntity;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstance;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstanceEntity;
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
	public Result wake(int id) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(id);
		return commandExecutor.execute(new WakeProcessCmd(entity));
	}
	/**
	 * 唤醒指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public Result wake(String procinstId) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new WakeProcessCmd(entity));
	}
	
	/**
	 * 挂起指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public Result suspend(int id) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(id);
		return commandExecutor.execute(new SuspendProcessCmd(entity));
	}
	/**
	 * 挂起指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public Result suspend(String procinstId) {
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
	public Result terminate(int id, String userId, String reason) {
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
	public Result terminate(String procinstId, String userId, String reason) {
		ProcessInstanceEntity entity = new ProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new TerminateProcessCmd(entity, userId, reason));
	}
	
	/**
	 * 删除指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public Result delete(int id) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(id);
		return commandExecutor.execute(new DeleteProcessCmd(entity));
	}
	/**
	 * 删除指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public Result delete(String procinstId) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new DeleteProcessCmd(entity));
	}
	
	/**
	 * 撤销删除指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public Result undoDelete(int id) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(id);
		return commandExecutor.execute(new UndoDeleteProcessCmd(entity));
	}
	/**
	 * 撤销删除指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public Result undoDelete(String procinstId) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new UndoDeleteProcessCmd(entity));
	}
	
	/**
	 * 物理删除指定id的流程实例
	 * @param id
	 * @return
	 */
	@Transaction
	public Result delete4Physical(int id) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(id);
		return commandExecutor.execute(new Delete4PhysicalProcessCmd(entity));
	}
	/**
	 * 物理删除指定id的流程实例
	 * @param procinstId
	 * @return
	 */
	@Transaction
	public Result delete4Physical(String procinstId) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new Delete4PhysicalProcessCmd(entity));
	}
	
	/**
	 * 恢复指定id的流程实例(从终止状态到运行状态)
	 * @param id
	 * @param active 是否直接恢复为活动状态
	 * @return
	 */
	@Transaction
	public Result recovery(int id, boolean active) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(id);
		return commandExecutor.execute(new RecoveryProcessCmd(entity, active));
	}
	/**
	 * 恢复指定id的流程实例(从终止状态到运行状态)
	 * @param procinstId
	 * @param active 是否直接恢复为活动状态
	 * @return
	 */
	@Transaction
	public Result recovery(String procinstId, boolean active) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new RecoveryProcessCmd(entity, active));
	}
	
	/**
	 * 激活指定id的流程实例(从完成状态到运行状态)
	 * @param id
	 * @param parameter
	 * @return
	 */
	@Transaction
	public Result activate(int id, ActivateParameter parameter) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(id);
		return commandExecutor.execute(new ActivateProcessCmd(entity, parameter));
	}
	/**
	 * 激活指定id的流程实例(从完成状态到运行状态)
	 * @param procinstId
	 * @param parameter
	 * @return
	 */
	@Transaction
	public Result activate(String procinstId, ActivateParameter parameter) {
		HistoryProcessInstanceEntity entity = new HistoryProcessInstanceEntity(procinstId);
		return commandExecutor.execute(new ActivateProcessCmd(entity, parameter));
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
	public Result start(StartParameter parameter) {
		return commandExecutor.execute(new StartProcessCmd(parameter));
	}
}
