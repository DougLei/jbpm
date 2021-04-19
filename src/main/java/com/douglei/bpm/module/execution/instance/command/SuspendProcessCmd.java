package com.douglei.bpm.module.execution.instance.command;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstanceEntity;
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
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance() != State.ACTIVE)
			return new Result("挂起失败, [%s]流程实例未处于[ACTIVE]状态", "jbpm.procinst.suspend.fail.state.error", processInstance.getTitle());
		
		SessionContext.getSqlSession().executeUpdate("update bpm_ru_procinst set state=? where id=?", Arrays.asList(State.SUSPENDED.getValue(), processInstance.getId()));
		return Result.getDefaultSuccessInstance();
	}
}
