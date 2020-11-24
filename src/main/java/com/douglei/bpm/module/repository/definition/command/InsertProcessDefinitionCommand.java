package com.douglei.bpm.module.repository.definition.command;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.CommandContext;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;

/**
 * 
 * @author DougLei
 */
public class InsertProcessDefinitionCommand implements Command<ExecutionResult<ProcessDefinition>> {
	private ProcessDefinition processDefinition;
	private boolean strict;
	
	public InsertProcessDefinitionCommand(ProcessDefinition processDefinition, boolean strict) {
		this.processDefinition = processDefinition;
		this.strict = strict;
	}

	@Override
	public ExecutionResult<ProcessDefinition> execute(CommandContext commandContext) {
		ProcessDefinition exProcessDefinition = commandContext.getSessionContext().getSQLSession().queryFirst(ProcessDefinition.class, "ProcessDefinition", "queryProcessDefinition4Save", processDefinition);
		if(exProcessDefinition == null) {
			// 新的流程定义, 进行save
			commandContext.getSessionContext().getTableSession().save(processDefinition); 
		}else {
			if(exProcessDefinition.getState() == ProcessDefinition.DELETE)
				return new ExecutionResult<ProcessDefinition>("已存在code为[%s], version为[%s]的流程定义", "bpm.process.defined.code.version.exists", processDefinition.getCode(), processDefinition.getVersion());
			
			if(exProcessDefinition.getSignature().equals(processDefinition.getSignature())){ // 没有修改流程定义的内容, 进行update
				processDefinition.setId(exProcessDefinition.getId());
				processDefinition.setSubversion(exProcessDefinition.getSubversion());
				processDefinition.setState(exProcessDefinition.getState());
				processDefinition.setContent(null);
				processDefinition.setSignature(null);
				commandContext.getSessionContext().getTableSession().update(processDefinition);
			}else if(!commandContext.getRuntimeModule().getInstanceService().exists(exProcessDefinition.getId()) && !commandContext.getHistoryModule().getInstanceService().exists(exProcessDefinition.getId())) { // 修改了内容, 但旧的流程定义不存在实例, 进行update
				processDefinition.setId(exProcessDefinition.getId());
				processDefinition.setSubversion(exProcessDefinition.getSubversion());
				processDefinition.setState(exProcessDefinition.getState());
				commandContext.getSessionContext().getTableSession().update(processDefinition); 
			}else { // 修改了内容, 且旧的流程定义存在实例, 根据参数strict的值, 进行save, 或提示操作失败
				if(!strict) 
					return new ExecutionResult<ProcessDefinition>("操作失败, 当前流程已存在实例", "bpm.process.defined.instance.exists", processDefinition.getName());
				
				processDefinition.setSubversion(exProcessDefinition.getSubversion()+1);
				processDefinition.setState(exProcessDefinition.getState());
				commandContext.getSessionContext().getTableSession().save(processDefinition); 
			}
		}
		
		if(processDefinition.getState() == ProcessDefinition.DEPLOY) 
			commandContext.getProcessHandler().addProcess(processDefinition);
		return new ExecutionResult<ProcessDefinition>(processDefinition, strict);
	}
}
