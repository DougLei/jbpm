package com.douglei.bpm.module.execution.instance.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.instance.history.HistoryProcessInstance;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstanceEntity;
import com.douglei.bpm.module.execution.task.history.HistoryAssignee;
import com.douglei.bpm.module.execution.task.history.HistoryCarbonCopy;
import com.douglei.bpm.module.execution.task.history.HistoryDispatch;
import com.douglei.bpm.module.execution.task.history.HistoryTask;
import com.douglei.bpm.module.execution.variable.history.HistoryVariable;
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
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance() != State.SUSPENDED)		
			return new Result("终止失败, [%s]流程实例未处于[SUSPENDED]状态", "jbpm.procinst.terminate.fail.state.error", processInstance.getTitle(), processInstance.getState());
		
		// 将流程实例从运行表转移到历史表
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_procinst where id=?", Arrays.asList(processInstance.getId()));
		SessionContext.getTableSession().save(new HistoryProcessInstance(processInstance, State.TERMINATED, userId, reason));
		
		// 将任务转移到历史表
		List<HistoryTask> tasks = SessionContext.getTableSession().query(HistoryTask.class, "select * from bpm_ru_task where procinst_id=?", Arrays.asList(processInstance.getProcinstId()));
		tasks.forEach(task -> task.setSourceTypeInstance(SourceType.BY_PROCINST_TERMINATED));
		SessionContext.getTableSession().save(tasks);
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_task where procinst_id=?", Arrays.asList(processInstance.getProcinstId()));
		
		// 将变量转移到历史表
		List<HistoryVariable> variables = SessionContext.getTableSession().query(HistoryVariable.class, "select * from bpm_ru_variable where procinst_id=?", Arrays.asList(processInstance.getProcinstId()));
		if(!variables.isEmpty()) {
			variables.forEach(variable -> variable.setSourceTypeInstance(SourceType.BY_PROCINST_TERMINATED));
			SessionContext.getTableSession().save(variables);
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where procinst_id=?", Arrays.asList(processInstance.getProcinstId()));
		}
		
		// 将指派信息转移到历史表
		List<HistoryAssignee> assignees = SessionContext.getSQLSession().query(HistoryAssignee.class, "TerminateProcess", "queryAssigneeList", tasks);
		if(!assignees.isEmpty()) {
			assignees.forEach(assignee -> assignee.setSourceTypeInstance(SourceType.BY_PROCINST_TERMINATED));
			SessionContext.getTableSession().save(assignees);
			SessionContext.getSQLSession().executeUpdate("TerminateProcess", "deleteAssignee", assignees);
		}
		
		// 将调度信息转移到历史表
		List<HistoryDispatch> dispatches = SessionContext.getSQLSession().query(HistoryDispatch.class, "TerminateProcess", "queryDispatchList", tasks);
		if(!dispatches.isEmpty()) {
			dispatches.forEach(dispatch -> dispatch.setSourceTypeInstance(SourceType.BY_PROCINST_TERMINATED));
			SessionContext.getTableSession().save(dispatches);
			SessionContext.getSQLSession().executeUpdate("TerminateProcess", "deleteDispatch", dispatches);
		}
		
		// 将抄送信息转移到历史表
		List<HistoryCarbonCopy> ccs = SessionContext.getSQLSession().query(HistoryCarbonCopy.class, "TerminateProcess", "queryCCList", tasks);
		if(!ccs.isEmpty()) {
			ccs.forEach(cc -> cc.setSourceTypeInstance(SourceType.BY_PROCINST_TERMINATED));
			SessionContext.getTableSession().save(ccs);
			SessionContext.getSQLSession().executeUpdate("TerminateProcess", "deleteCC", ccs);
		}
		
		return Result.getDefaultSuccessInstance();
	}
}
