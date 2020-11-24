package com.douglei.bpm.module.repository.type.command;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.CommandContext;
import com.douglei.bpm.module.repository.type.entity.ProcessType;

/**
 * 
 * @author DougLei
 */
public class InsertProcessTypeCommand implements Command<ExecutionResult<ProcessType>> {
	private ProcessType type;
	public InsertProcessTypeCommand(ProcessType type) {
		this.type = type;
	}

	@Override
	public ExecutionResult<ProcessType> execute(CommandContext commandContext) {
		if(commandContext.getSessionContext().getSQLSession().uniqueQuery_("ProcessType", "query4ValidateCodeExists", type) != null)
			return new ExecutionResult<ProcessType>("已存在编码为[%s]的流程类型", "bpm.process.type.code.exists", type.getCode());
		
		commandContext.getSessionContext().getTableSession().save(type);
		return new ExecutionResult<ProcessType>(type);
	}
}
