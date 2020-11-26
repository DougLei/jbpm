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
public class UnDeployProcessCommand extends AbstractProcessDefinitionCommand<ExecutionResult<Integer>> {
	private int processDefinitionId;
	private InstanceHandlePolicy runtimeInstancePolicy;
	private InstanceHandlePolicy historyInstancePolicy;
	
	public UnDeployProcessCommand(int processDefinitionId, InstanceHandlePolicy runtimeInstancePolicy, InstanceHandlePolicy historyInstancePolicy) {
		this.processDefinitionId = processDefinitionId;
		this.runtimeInstancePolicy = runtimeInstancePolicy;
		this.historyInstancePolicy = historyInstancePolicy;
	}

	@Override
	public ExecutionResult<Integer> execute(CommandContext commandContext) {
		ProcessDefinition processDefinition = commandContext.getSessionContext().getTableSession().uniqueQuery(ProcessDefinition.class, "select id, code, version, subversion, state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
		if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult<Integer>("操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
		if(processDefinition.getState() == ProcessDefinition.UNDEPLOY)
			return new ExecutionResult<Integer>("操作失败, 当前流程未部署", "bpm.process.defined.undeploy", processDefinitionId);
		
		if(runtimeInstancePolicy != null && commandContext.getRuntimeModule().getInstanceService().exists(processDefinitionId)) 
			commandContext.getRuntimeModule().getInstanceService().handle(processDefinitionId, runtimeInstancePolicy);
		
		if(historyInstancePolicy != null && commandContext.getHistoryModule().getInstanceService().exists(processDefinitionId)) 
			commandContext.getHistoryModule().getInstanceService().handle(processDefinitionId, historyInstancePolicy);
		
		updateState(commandContext, processDefinitionId, ProcessDefinition.UNDEPLOY);
		commandContext.getProcessHandler().deleteProcess(processDefinitionId);
		return new ExecutionResult<Integer>(processDefinitionId, runtimeInstancePolicy, historyInstancePolicy);
	}
}
