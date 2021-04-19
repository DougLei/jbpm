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
public class SuspendProcessCmd extends WakeProcessCmd {
	
	public SuspendProcessCmd(ProcessInstanceEntity entity) {
		super(entity);
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance() != State.ACTIVE)
			return new ExecutionResult("挂起失败, [%s]流程实例未处于[ACTIVE]状态", "jbpm.procinst.suspend.fail.state.error", processInstance.getTitle());
		
		SessionContext.getSqlSession().executeUpdate("update bpm_ru_procinst set state=? where id=?", Arrays.asList(State.SUSPENDED.getValue(), processInstance.getId()));
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
