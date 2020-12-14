package com.douglei.bpm.module.runtime.instance;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.repository.definition.entity.State;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.handler.ProcessHandlers;
import com.douglei.bpm.process.handler.event.start.StartEventExecuteParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessInstanceService {
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	@Autowired
	private ProcessHandlers processExecutors;
	
	/**
	 * 启动流程
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult start(StartParameter parameter) {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().uniqueQuery(ProcessDefinition.class, "ProcessDefinition", "query4Start", parameter);
		switch (parameter.getMode()) {
			case StartParameter.BY_PROCESS_DEFINITION_ID:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在id为["+parameter.getProcessDefinitionId()+"]的流程");
				break;
			case StartParameter.BY_PROCESS_DEFINITION_CODE:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在code为["+parameter.getCode()+"]的流程; 或未设置其流程的主版本");
				break;
			case StartParameter.BY_PROCESS_DEFINITION_CODE_VERSION:
				if(processDefinition == null) 
					return new ExecutionResult("启动失败, 不存在code为["+parameter.getCode()+"], version为["+parameter.getVersion()+"]的流程");
				break;
		}
		if(processDefinition.getStateInstance() != State.DEPLOY)
			return new ExecutionResult("启动失败, ["+processDefinition.getName()+"]流程还未部署");
		
		ProcessMetadata processMetadata = processContainer.getProcess(processDefinition.getId());
		return processExecutors.startup(processMetadata.getStartEvent(), new StartEventExecuteParameter(processMetadata, parameter));
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
	 * 判断指定的流程定义, 是否存在运行实例
	 * @param processDefinition
	 * @return
	 */
	public boolean exists(ProcessDefinition processDefinition) {
		// TODO 
		return false;
	}
	
	/**
	 * 处理指定的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(ProcessDefinition processDefinition, ProcessInstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
	}
}
