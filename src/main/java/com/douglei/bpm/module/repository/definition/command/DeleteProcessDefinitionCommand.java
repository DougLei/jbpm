package com.douglei.bpm.module.repository.definition.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.context.CommandContext;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;

/**
 * 
 * @author DougLei
 */
public class DeleteProcessDefinitionCommand extends AbstractProcessDefinitionCommand<ExecutionResult<Integer>> {
	private int processDefinitionId;
	private boolean strict;
	
	public DeleteProcessDefinitionCommand(int processDefinitionId, boolean strict) {
		this.processDefinitionId = processDefinitionId;
		this.strict = strict;
	}

	@Override
	public ExecutionResult<Integer> execute(CommandContext commandContext) {
		List<Object> paramList = Arrays.asList(processDefinitionId);
		
		ProcessDefinition processDefinition = commandContext.getSessionContext().getTableSession().uniqueQuery(ProcessDefinition.class, "select name, state from bpm_re_procdef where id=?", paramList);
		if(processDefinition == null || processDefinition.getState() == ProcessDefinition.DELETE)
			return new ExecutionResult<Integer>("操作失败, 不存在id为[%d]的流程定义", "bpm.process.defined.id.unexists", processDefinitionId);
		if(processDefinition.getState() == ProcessDefinition.DEPLOY)
			return new ExecutionResult<Integer>("操作失败, 当前流程已部署, 请先取消部署", "bpm.process.defined.undeploy.first", processDefinitionId);
		
		if(commandContext.getRuntimeModule().getInstanceService().exists(processDefinitionId) || commandContext.getHistoryModule().getInstanceService().exists(processDefinitionId)) {
			if(!strict)
				return new ExecutionResult<Integer>("操作失败, 当前流程已存在实例", "bpm.process.defined.instance.exists");
			updateState(commandContext.getSessionContext(), processDefinitionId, ProcessDefinition.DELETE);
		} else {
			commandContext.getSessionContext().getSqlSession().executeUpdate("delete bpm_re_procdef where id=?", paramList);
		}
		return new ExecutionResult<Integer>(processDefinitionId, strict);
	}
}
