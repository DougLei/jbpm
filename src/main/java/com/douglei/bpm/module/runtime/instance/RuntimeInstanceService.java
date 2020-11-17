package com.douglei.bpm.module.runtime.instance;

import com.douglei.bpm.bean.annotation.Attribute;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.process.ProcessHandler;
import com.douglei.bpm.process.executor.Process;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean
public class RuntimeInstanceService {
	
	@Attribute
	private ProcessHandler processHandler;
	
	/**
	 * 判断指定id的流程定义, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public boolean exists(int processDefinitionId) {
		// TODO 判断指定的流程定义id, 是否存在运行实例
		
		return true;
	}

	/**
	 * 处理指定id的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	@Transaction
	public void process(int processDefinitionId, InstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
		
	}
	
	/**
	 * 启动流程
	 * @param starter
	 * @return 
	 */
	@Transaction
	public ExecutionResult<ProcessRuntimeInstance> start(ProcessStarter starter) {
		int processDefinitionId = starter.getProcessDefinitionIdAfterValidate(SessionContext.getSqlSession());
		Process process = processHandler.get(processDefinitionId);
		return process.executeStart();
	}
	
	/**
	 * 激活指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult activate(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 挂起指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult suspend(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 终止指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult terminate(int instanceId) {
		// TODO 
		
		return null;
	}
	
	/**
	 * 删除指定id的流程实例
	 * @param instanceId
	 * @return 返回null表示操作成功
	 */
	@Transaction
	public ExecutionResult delete(int instanceId) {
		// TODO 
		
		return null;
	}
}
