package com.douglei.bpm.module.execution.instance.command;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.instance.command.parameter.ActivateParameter;
import com.douglei.bpm.module.execution.instance.history.HistoryProcessInstanceEntity;

/**
 * 
 * @author DougLei
 */
public class ActivateProcessCmd extends DeleteProcessCmd {
	private ActivateParameter parameter;
	
	public ActivateProcessCmd(HistoryProcessInstanceEntity entity, ActivateParameter parameter) {
		super(entity);
		this.parameter= parameter;
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance() != State.FINISHED) 
			return new Result("激活失败, [%s]流程实例未处于[FINISHED]状态", "jbpm.procinst.activate.fail.state.error", processInstance.getTitle());
			
		// TODO 
		
		
		return Result.getDefaultSuccessInstance();
	}
}
