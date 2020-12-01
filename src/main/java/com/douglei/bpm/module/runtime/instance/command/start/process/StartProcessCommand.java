package com.douglei.bpm.module.runtime.instance.command.start.process;

import java.util.Date;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.components.ProcessObjectException;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.runtime.instance.entity.ProcessInstance;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.executor.Executors;
import com.douglei.bpm.process.executor.event.start.StartEventExecutionParameter;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 启动流程命令
 * @author DougLei
 */
public class StartProcessCommand implements Command<ProcessInstance> {
	private StartParameter parameter;
	public StartProcessCommand(StartParameter parameter) {
		this.parameter = parameter;
	}
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	@Autowired
	private Executors executors;
	
	public ProcessInstance execute() {
		ProcessMetadata process = getProcessMetadata();
		ProcessInstance processInstance = createProcessInstance(process);
		executors.execute(process.getStartEvent(), new StartEventExecutionParameter(process.getId(), processInstance.getId(), parameter));
		return processInstance;
	}
	
	// 获取流程对象
	private ProcessMetadata getProcessMetadata() {
		ProcessDefinition processDefinition = SessionContext.getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "query4Start", parameter);
		switch (parameter.getMode()) {
			case BY_PROCESS_DEFINITION_ID:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					throw new ProcessObjectException("启动失败, 不存在id为["+parameter.getProcessDefinitionId()+"]的流程");
				break;
			case BY_PROCESS_DEFINITION_CODE_VERSION:
				if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE) 
					throw new ProcessObjectException("启动失败, 不存在code为["+parameter.getCode()+"], version为["+parameter.getVersion()+"]的流程");
				break;
		}
		if(processDefinition.getState() == ProcessDefinition.UNDEPLOY)
			throw new ProcessObjectException("启动失败, ["+processDefinition.getName()+"]流程还未部署");
		return processContainer.getProcess(processDefinition.getId());
	}

	// 创建流程实例
	private ProcessInstance createProcessInstance(ProcessMetadata process) {
		ProcessInstance instance = new ProcessInstance();
		instance.setProcdefId(process.getId());
		instance.setTitle(process.getTitle(parameter.getVariables()));
		instance.setBusinessId(parameter.getBusinessId());
		instance.setPageId(process.getPageID());
		instance.setStartUserId(parameter.getStartUserId());
		instance.setStartTime(new Date());
		instance.setTenantId(parameter.getTenantId());
		
		SessionContext.getTableSession().save(instance);
		return instance;
	}
}
