package com.douglei.bpm.module.runtime.instance;

import com.douglei.bpm.bean.Autowire;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.CommandExecutor;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;
import com.douglei.bpm.module.runtime.instance.command.ProcessStartCommand;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean(isTransaction = false)
public class RuntimeInstanceService {
	
	@Autowire
	private CommandExecutor commandExecutor;
	
	/**
	 * 启动流程
	 * @param parameter
	 * @return 
	 */
	public ExecutionResult<ProcessRuntimeInstance> start(StartParameter parameter) {
		return commandExecutor.execute(new ProcessStartCommand(parameter));
	}
	
	/**
	 * 处理指定id的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(int processDefinitionId, InstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
		
	}
	
	/**
	 * 激活指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	public ExecutionResult<Integer> activate(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 挂起指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	public ExecutionResult<Integer> suspend(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 终止指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	public ExecutionResult<Integer> terminate(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 删除指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	public ExecutionResult<Integer> delete(int instanceId) {
		// TODO 
		
		return null;
	}
}
