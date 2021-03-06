package com.douglei.bpm.module.execution.instance.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.bean.PropertyValueCopier;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.instance.State;
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
public class RecoveryProcessCmd extends DeleteProcessCmd {
	private boolean active; // 是否直接恢复为活动状态
	
	public RecoveryProcessCmd(HistoryProcessInstanceEntity entity, boolean active) {
		super(entity);
		this.active= active;
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance() != State.TERMINATED) 
			return new Result("恢复失败, [%s]流程实例未处于[TERMINATED]状态", "jbpm.procinst.recovery.fail.state.error", processInstance.getTitle());
		
		// 将流程实例从历史表转移到运行表
		SessionContext.getSqlSession().executeUpdate("delete bpm_hi_procinst where id=?", Arrays.asList(processInstance.getId()));
		ProcessInstance instance = new ProcessInstance();
		PropertyValueCopier.copy(processInstance, instance);
		instance.setSuspendTime(active?null:processInstance.getSuspendTime());
		instance.setStateInstance(active?State.ACTIVE:State.SUSPENDED);
		SessionContext.getTableSession().save(instance);
		
		// 将任务转移到运行表
		List<Task> tasks = SessionContext.getTableSession().query(Task.class, "select * from bpm_hi_task where procinst_id=? and source_type=?", Arrays.asList(processInstance.getProcinstId(), SourceType.BY_PROCINST_TERMINATED.getValue()));
		if(active) {
			// 批量计算任务的截止日期
			List<Task> tasks_ = new ArrayList<Task>(tasks.size());
			tasks.stream()
				.filter(task -> task.getSuspendTime()==null && task.getExpiryTime() != null)
				.forEach(task -> tasks_.add(task));
			
			if(!tasks_.isEmpty())
				new WakeProcessCmd().updateExpiryTimes(processInstance.getProcdefId(), processInstance.getSuspendTime(), tasks_, processEngineBeans);
		}
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
			SessionContext.getSQLSession().executeUpdate("RecoveryProcess", "deleteAssignee", assignees);
			SessionContext.getTableSession().save(assignees);
		}
		
		// 将调度信息转移到历史表
		List<Dispatch> dispatches = SessionContext.getSQLSession().query(Dispatch.class, "RecoveryProcess", "queryDispatchList", tasks);
		if(!dispatches.isEmpty()) {
			SessionContext.getSQLSession().executeUpdate("RecoveryProcess", "deleteDispatch", dispatches);
			SessionContext.getTableSession().save(dispatches);
		}
		
		// 将抄送信息转移到历史表
		List<CarbonCopy> ccs = SessionContext.getSQLSession().query(CarbonCopy.class, "RecoveryProcess", "queryCCList", tasks);
		if(!ccs.isEmpty()) {
			SessionContext.getSQLSession().executeUpdate("RecoveryProcess", "deleteCC", ccs);
			SessionContext.getTableSession().save(ccs);
		}
		
		return Result.getDefaultSuccessInstance();
	}
}
