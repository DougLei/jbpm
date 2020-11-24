package com.douglei.bpm.module.repository.type.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.CommandContext;

/**
 * 
 * @author DougLei
 */
public class DeleteProcessTypeCommand implements Command<ExecutionResult<Integer>> {
	private int processTypeId; 
	private boolean strict;
	
	public DeleteProcessTypeCommand(int processTypeId, boolean strict) {
		this.processTypeId = processTypeId;
		this.strict = strict;
	}

	@Override
	public ExecutionResult<Integer> execute(CommandContext commandContext) {
		List<Object> paramList = Arrays.asList(processTypeId);
		
		int count = Integer.parseInt(commandContext.getSessionContext().getSqlSession().uniqueQuery_("select count(id) from bpm_re_procdef where type_id = ?", paramList)[0].toString());
		if(count > 0 && !strict)
			return new ExecutionResult<Integer>("该流程类型关联了[%d]条流程, 无法删除", "bpm.process.type.delete.fail", count);

		commandContext.getSessionContext().getSqlSession().executeUpdate("delete bpm_re_proctype where id=?", paramList);
		if(count > 0) 
			commandContext.getSessionContext().getSqlSession().executeUpdate("update bpm_re_procdef set type_id=0 where type_id=?", paramList);
		return new ExecutionResult<Integer>(processTypeId, strict);
	}
}
