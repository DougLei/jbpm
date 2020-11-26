package com.douglei.bpm.module.repository.definition.command;

import java.util.Arrays;

import com.douglei.bpm.module.components.command.Command;
import com.douglei.bpm.module.components.command.context.CommandContext;

/**
 * 
 * @author DougLei
 */
public abstract class AbstractProcessDefinitionCommand<T> implements Command<T> {
	
	/**
	 * 更新流程定义的状态
	 * @param commandContext
	 * @param processDefinitionId
	 * @param state {@link ProcessDefinitionStateConstants}
	 */
	protected void updateState(CommandContext commandContext, int processDefinitionId, byte state) {
		commandContext.getSessionContext().getSqlSession().executeUpdate("update bpm_re_procdef set state=? where id=?", Arrays.asList(state, processDefinitionId));
	}
}
