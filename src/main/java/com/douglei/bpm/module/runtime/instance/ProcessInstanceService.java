package com.douglei.bpm.module.runtime.instance;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.CommandExecutor;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.command.StartParameter;
import com.douglei.bpm.module.runtime.instance.command.StartProcessCommand;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessInstanceService {
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	/**
	 * 启动流程
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult start(StartParameter parameter) {
		return commandExecutor.execute(new StartProcessCommand(parameter));
	}
	
	/**
	 * 激活指定id的流程实例
	 * @param instanceId
	 * @return
	 */
	public int activate(int instanceId) {
		// TODO 
		
		return 1;
	}
	
	/**
	 * 挂起指定id的流程实例
	 * @param instanceId
	 * @return
	 */
	public int suspend(int instanceId) {
		// TODO 
		
		return 1;
	}
	
	/**
	 * 终止指定id的流程实例
	 * @param instanceId
	 * @return
	 */
	public int terminate(int instanceId) {
		// TODO 
		
		return 1;
	}
	
	/**
	 * 删除指定id的流程实例
	 * @param instanceId
	 * @return
	 */
	public int delete(int instanceId) {
		// TODO 
		
		return 1;
	}
	
	/**
	 * 判断指定id的流程, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	public boolean exists(int processDefinitionId) {
		// TODO 
		return false;
	}
	
	/**
	 * 处理指定id的流程, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(int processDefinitionId, ProcessInstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
	}
	
	/**
	 * 判断指定code和version的流程, 是否存在运行实例
	 * @param code
	 * @param version
	 * @param tenantId
	 * @return
	 */
	public boolean exists(String code, String version, String tenantId) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 处理指定code和version的流程, 相关的所有运行实例
	 * @param code
	 * @param version
	 * @param tenantId
	 * @param policy 对实例的处理策略
	 */
	public void handle(String code, String version, String tenantId, ProcessInstanceHandlePolicy policy) {
		// TODO Auto-generated method stub
		
	}
}
