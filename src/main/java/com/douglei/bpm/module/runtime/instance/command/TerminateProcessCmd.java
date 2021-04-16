package com.douglei.bpm.module.runtime.instance.command;

import java.util.Arrays;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.instance.HistoryProcessInstance;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceEntity;
import com.douglei.bpm.module.runtime.instance.State;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class TerminateProcessCmd extends WakeProcessCmd {
	private String userId; 
	private String reason; 
	
	public TerminateProcessCmd(ProcessInstanceEntity entity, String userId, String reason) {
		super(entity);
		this.userId = userId;
		this.reason = reason;
	}

	@Override
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(!processInstance.getStateInstance().supportTerminate())		
			return new ExecutionResult("终止失败, [%s]流程实例处于[%s]状态", "jbpm.procinst.terminate.fail.state.error", processInstance.getTitle(), processInstance.getState());
		
		// 将实例从运行表转移到历史表
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_procinst where id=?", Arrays.asList(processInstance.getId()));
		SessionContext.getTableSession().save(new HistoryProcessInstance(processInstance, State.TERMINATED, userId, reason));
		
		// 将任务转移到历史表
		SessionContext.getTableSession().query(Task.class, "select * from bpm_ru_task task where procinst_id=?", Arrays.asList(a))
		
		// 将指派等用户信息转移到历史表
		
		// 将变量转移到历史表
		
		
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
