package com.douglei.bpm.module.runtime.instance.command;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceEntity;
import com.douglei.bpm.module.runtime.instance.State;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class DeleteProcessCmd extends WakeProcessCmd {
	
	public DeleteProcessCmd(ProcessInstanceEntity entity) {
		super(entity);
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance().supportDelete()) {
			SessionContext.getSqlSession().executeUpdate("update bpm_hi_procinst set state=? where id=?", Arrays.asList(State.DELETE.name(), processInstance.getId()));
			return ExecutionResult.getDefaultSuccessInstance();
		}
		return new ExecutionResult("删除失败, [%s]流程实例处于[%s]状态", "jbpm.procinst.delete.fail.state.error", processInstance.getTitle());
	}
}
