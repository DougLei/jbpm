package com.douglei.bpm.module.runtime.instance.command;

import java.util.Arrays;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.CommandContext;
import com.douglei.bpm.module.components.command.context.transaction.SessionContext;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.module.runtime.instance.entity.ProcessRuntimeInstance;
import com.douglei.bpm.module.runtime.instance.start.StartParameter;
import com.douglei.bpm.module.runtime.instance.start.StartingMode;
import com.douglei.bpm.process.node.Process;
import com.douglei.bpm.process.node.task.event.StartEvent;

/**
 * 启动流程命令
 * @author DougLei
 */
public class StartProcessCommand implements Command<ExecutionResult<ProcessRuntimeInstance>> {
	private StartParameter parameter;
	
	public StartProcessCommand(StartParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public ExecutionResult<ProcessRuntimeInstance> execute(CommandContext commandContext) {
		int processDefinitionId = getProcessDefinitionIdAfterValidate(commandContext.getSessionContext());
		Process process = commandContext.getProcessHandler().getProcess(processDefinitionId);
		
		StartEvent startEvent = process.getStartEvent();
		
		
		
		
		return null;
	}

	// 获取验证后的流程定义id
	private int getProcessDefinitionIdAfterValidate(SessionContext sessionContext) {
		if(parameter.getStartingMode() == StartingMode.BY_PROCESS_DEFINITION_ID) {
			Object[] processDefinition = sessionContext.getSqlSession().uniqueQuery_("select state from bpm_re_procdef where id=?", Arrays.asList(parameter.getProcessDefinitionId()));
			if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinition.DELETE)
				throw new NullPointerException("启动失败, 不存在id为["+parameter.getProcessDefinitionId()+"]的流程定义");
			if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinition.UNDEPLOY)
				throw new IllegalArgumentException("启动失败, id为["+parameter.getProcessDefinitionId()+"]的流程定义还未发布");
			return parameter.getProcessDefinitionId();
		}
		
		if(parameter.getStartingMode() == StartingMode.BY_PROCESS_DEFINITION_CODE_VERSION) {
			Object[] processDefinition = sessionContext.getSqlSession().queryFirst_("select state, id from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(parameter.getCode(), parameter.getVersion()));
			if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinition.DELETE)
				throw new NullPointerException("启动失败, 不存在code为["+parameter.getCode()+"], version为["+parameter.getVersion()+"]的流程定义");
			if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinition.UNDEPLOY)
				throw new IllegalArgumentException("启动失败, code为["+parameter.getCode()+"], version为["+parameter.getVersion()+"]的流程定义还未发布");
			return Integer.parseInt(processDefinition[1].toString());
		}
		
		throw new IllegalArgumentException("不支持使用["+parameter.getStartingMode()+"]方式启动流程");
	}
}
