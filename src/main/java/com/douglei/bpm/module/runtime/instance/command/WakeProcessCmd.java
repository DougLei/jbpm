package com.douglei.bpm.module.runtime.instance.command;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceEntity;
import com.douglei.bpm.module.runtime.instance.State;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class WakeProcessCmd implements Command {
	protected ProcessInstance processInstance;
	
	public WakeProcessCmd(ProcessInstanceEntity entity) {
		this.processInstance = entity.getProcessInstance();
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance() != State.SUSPENDED)
			return new ExecutionResult("唤醒失败, [%s]流程实例未处于[SUSPENDED]状态", "jbpm.procinst.wake.fail.state.error", processInstance.getTitle());
		
		SessionContext.getSqlSession().executeUpdate("update bpm_ru_procinst set state=? where id=?", Arrays.asList(State.ACTIVE.getValue(), processInstance.getId()));
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
