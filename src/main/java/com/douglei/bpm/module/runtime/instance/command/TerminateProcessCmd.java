package com.douglei.bpm.module.runtime.instance.command;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceEntity;
import com.douglei.bpm.module.runtime.instance.command.parameter.TerminateParameter;
import com.douglei.orm.context.SessionContext;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public class TerminateProcessCmd implements Command {
	private ProcessInstanceEntity entity;
	private TerminateParameter parameter;
	
	public TerminateProcessCmd(ProcessInstanceEntity entity, TerminateParameter parameter) {
		this.entity = entity;
		this.parameter = parameter;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		// TODO Auto-generated method stub
		
		
		
		
//		if(StringUtil.isEmpty(userId) || StringUtil.isEmpty(reason))
//			throw new RuntimeException("终止失败, 进行终止操作的用户id和终止原因不能为空");
//		
//		ProcessInstance processInstance = SessionContext.getTableSession().uniqueQuery(ProcessInstance.class, "select title, state from bpm_ru_procinst where id=?", Arrays.asList(id));
//		if(processInstance == null)
//			throw new RuntimeException("终止失败, 不存在id为["+id+"]的流程实例");
//		if(!processInstance.getStateInstance().supportTerminate())
//			return new ExecutionResult("终止失败, [%s]流程实例处于[%s]状态", "jbpm.procinst.terminate.fail.state.error", processInstance.getTitle(), processInstance.getState());
//		
//		
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
