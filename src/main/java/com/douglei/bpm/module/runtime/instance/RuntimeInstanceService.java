package com.douglei.bpm.module.runtime.instance;

import java.util.Arrays;

import com.douglei.bpm.bean.Autowire;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;
import com.douglei.bpm.process.ProcessHandler;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean
public class RuntimeInstanceService {
	
	@Autowire
	private ProcessHandler processHandler;
	
	/**
	 * 判断指定id的流程定义, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public boolean exists(int processDefinitionId) {
		return Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_ru_procinst where procdef_id=?", Arrays.asList(processDefinitionId))[0].toString()) > 0;
	}

	/**
	 * 处理指定id的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	@Transaction
	public void handle(int processDefinitionId, InstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
		
	}
	
	/**
	 * 启动流程
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult<ProcessRuntimeInstance> start(StartParameter parameter) {
//		int processDefinitionId = parameter.getProcessDefinitionIdAfterDBValidate(SessionContext.getSqlSession());
//		Process process = processHandler.get(processDefinitionId);
//		
//		// TODO 
//		return process.start(starter);
		return null;
	}
	
//	/**
//	 * 激活指定id的流程实例
//	 * @param instanceId
//	 * @return 返回null表示操作成功
//	 */
//	@Transaction
//	public ExecutionResult activate(int instanceId) {
//		// TODO 
//		
//		return null;
//	}
//	
//	/**
//	 * 挂起指定id的流程实例
//	 * @param instanceId
//	 * @return 返回null表示操作成功
//	 */
//	@Transaction
//	public ExecutionResult suspend(int instanceId) {
//		// TODO 
//		
//		return null;
//	}
//	
//	/**
//	 * 终止指定id的流程实例
//	 * @param instanceId
//	 * @return 返回null表示操作成功
//	 */
//	@Transaction
//	public ExecutionResult terminate(int instanceId) {
//		// TODO 
//		
//		return null;
//	}
//	
//	/**
//	 * 删除指定id的流程实例
//	 * @param instanceId
//	 * @return 返回null表示操作成功
//	 */
//	@Transaction
//	public ExecutionResult delete(int instanceId) {
//		// TODO 
//		
//		return null;
//	}
}
