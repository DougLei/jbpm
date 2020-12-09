package com.douglei.bpm.module.runtime.instance;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.runtime.instance.entity.ProcessInstance;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.scheduler.ProcessScheduler;
import com.douglei.bpm.process.scheduler.event.start.StartEventDispatchParameter;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 运行实例服务
 * @author DougLei
 */
@SuppressWarnings("unchecked")
@Bean(isTransaction=true)
public class ProcessInstanceService {
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	@Autowired
	private ProcessScheduler processScheduler;
	
	/**
	 * 启动流程
	 * @param parameter
	 * @return 
	 */
	@Transaction
	public ExecutionResult start(StartParameter parameter) {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "query4Start", parameter);
		switch (parameter.getStartMode()) {
			case StartParameter.BY_PROCESS_DEFINITION_ID:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					return new ExecutionResult("启动失败, 不存在id为["+parameter.getProcessDefinitionId()+"]的流程");
				break;
			case StartParameter.BY_PROCESS_DEFINITION_CODE:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					return new ExecutionResult("启动失败, 不存在code为["+parameter.getCode()+"]的流程");
				break;
			case StartParameter.BY_PROCESS_DEFINITION_CODE_VERSION:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					return new ExecutionResult("启动失败, 不存在code为["+parameter.getCode()+"], version为["+parameter.getVersion()+"]的流程");
				break;
		}
		if(processDefinition.getState() == ProcessDefinition.UNDEPLOY)
			return new ExecutionResult("启动失败, ["+processDefinition.getName()+"]流程还未部署");
		
		ProcessMetadata processMetadata = processContainer.getProcess(processDefinition.getId());
		return processScheduler.dispatchTask(processMetadata.getStartEvent(), new StartEventDispatchParameter(processMetadata, parameter));
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
	 * 处理指定id的流程定义, 相关的所有运行实例
	 * @param processDefinitionId
	 * @param policy 对实例的处理策略
	 */
	public void handle(int processDefinitionId, ProcessInstanceHandlePolicy policy) {
		// TODO 处理指定id的流程定义, 相关的所有运行实例
		
	}
	
	/**
	 * 判断指定id的流程定义, 是否存在运行实例
	 * @param processDefinitionId
	 * @return
	 */
	public boolean exists(int processDefinitionId) {
//		return Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_ru_procinst where procdef_id=?", Arrays.asList(processDefinitionId))[0].toString()) > 0;
		return false;
	}
}
