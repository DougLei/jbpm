package com.douglei.bpm.module.repository.definition.command;

import java.util.Arrays;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.context.CommandContext;
import com.douglei.bpm.module.components.instance.InstanceHandlePolicy;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;

/**
 * 
 * @author DougLei
 */
public class DeployProcessCommand extends AbstractProcessDefinitionCommand<ExecutionResult<Integer>> {
	private int processDefinitionId;
	private InstanceHandlePolicy runtimeInstancePolicy;
	
	public DeployProcessCommand(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy) {
		this.processDefinitionId = processDefinitionId;
		this.runtimeInstancePolicy = runtimeInstancePolicy;
	}

	@Override
	public ExecutionResult<Integer> execute(CommandContext commandContext) {
		ProcessDefinition processDefinition = commandContext.getSessionContext().getTableSession().uniqueQuery(ProcessDefinition.class, "select id, state, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult<Integer>("操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
		if(processDefinition.getState() == ProcessDefinition.DEPLOY)
			return new ExecutionResult<Integer>("操作失败, 当前流程已部署", "bpm.process.defined.already.deploy", processDefinitionId);
		
		if(runtimeInstancePolicy != null && commandContext.getRuntimeModule().getInstanceService().exists(processDefinitionId))
			commandContext.getRuntimeModule().getInstanceService().handle(processDefinitionId, runtimeInstancePolicy);
		
		updateState(commandContext.getSessionContext(), processDefinitionId, ProcessDefinition.DEPLOY);
		commandContext.getProcessHandler().addProcess(processDefinition);
		return new ExecutionResult<Integer>(processDefinitionId, runtimeInstancePolicy);
	}
}
