package com.douglei.bpm.module.execution.instance.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.bean.PropertyValueCopier;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.instance.command.parameter.ActivateParameter;
import com.douglei.bpm.module.execution.instance.history.HistoryProcessInstanceEntity;
import com.douglei.bpm.module.execution.instance.runtime.ProcessInstance;
import com.douglei.bpm.module.execution.task.runtime.Assignee;
import com.douglei.bpm.module.execution.task.runtime.CarbonCopy;
import com.douglei.bpm.module.execution.task.runtime.Dispatch;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.module.execution.variable.runtime.Variable;
import com.douglei.orm.context.SessionContext;

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
			
		// 将流程实例从历史表转移到运行表
		SessionContext.getSqlSession().executeUpdate("delete bpm_hi_procinst where id=?", Arrays.asList(processInstance.getId()));
		ProcessInstance instance = new ProcessInstance();
		PropertyValueCopier.copy(processInstance, instance);
		instance.setStateInstance(State.ACTIVE);
		SessionContext.getTableSession().save(instance);
		
		// 将任务转移到运行表
		List<Task> tasks = SessionContext.getTableSession().query(Task.class, "select * from bpm_hi_task where procinst_id=? and source_type=?", Arrays.asList(processInstance.getProcinstId(), SourceType.BY_PROCINST_TERMINATED.getValue()));
		SessionContext.getTableSession().save(tasks);
		SessionContext.getSqlSession().executeUpdate("delete bpm_hi_task where procinst_id=? and source_type=?", Arrays.asList(processInstance.getProcinstId(), SourceType.BY_PROCINST_TERMINATED.getValue()));
		
		// 将变量转移到历史表
		List<Variable> variables = SessionContext.getTableSession().query(Variable.class, "select * from bpm_hi_variable where procinst_id=? and source_type=?", Arrays.asList(processInstance.getProcinstId(), SourceType.BY_PROCINST_TERMINATED.getValue()));
		if(!variables.isEmpty()) {
			SessionContext.getTableSession().save(variables);
			SessionContext.getSqlSession().executeUpdate("delete bpm_hi_variable where procinst_id=? and source_type=?", Arrays.asList(processInstance.getProcinstId(), SourceType.BY_PROCINST_TERMINATED.getValue()));
		}
		
		// 将指派信息转移到历史表
		List<Assignee> assignees = SessionContext.getSQLSession().query(Assignee.class, "RecoveryProcess", "queryAssigneeList", tasks);
		if(!assignees.isEmpty()) {
			SessionContext.getTableSession().save(assignees);
			SessionContext.getSQLSession().executeUpdate("RecoveryProcess", "deleteAssignee", assignees);
		}
		
		// 将调度信息转移到历史表
		List<Dispatch> dispatches = SessionContext.getSQLSession().query(Dispatch.class, "RecoveryProcess", "queryDispatchList", tasks);
		if(!dispatches.isEmpty()) {
			SessionContext.getTableSession().save(dispatches);
			SessionContext.getSQLSession().executeUpdate("RecoveryProcess", "deleteDispatch", dispatches);
		}
		
		// 将抄送信息转移到历史表
		List<CarbonCopy> ccs = SessionContext.getSQLSession().query(CarbonCopy.class, "RecoveryProcess", "queryCCList", tasks);
		if(!ccs.isEmpty()) {
			SessionContext.getTableSession().save(ccs);
			SessionContext.getSQLSession().executeUpdate("RecoveryProcess", "deleteCC", ccs);
		}
		
		
		
		return Result.getDefaultSuccessInstance();
	}
}
