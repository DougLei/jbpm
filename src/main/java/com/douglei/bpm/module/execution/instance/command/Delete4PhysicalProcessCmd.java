package com.douglei.bpm.module.execution.instance.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.history.HistoryProcessInstanceEntity;
import com.douglei.bpm.module.execution.task.history.HistoryTask;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class Delete4PhysicalProcessCmd extends DeleteProcessCmd {
	
	public Delete4PhysicalProcessCmd(HistoryProcessInstanceEntity entity) {
		super(entity);
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(!processInstance.getStateInstance().supportPhysicalDelete()) 
			return new Result("删除失败, [%s]流程实例处于[%s]状态", "jbpm.procinst.physical.delete.fail.state.error", processInstance.getTitle());
		
		// 删除流程实例
		SessionContext.getSqlSession().executeUpdate("delete bpm_hi_procinst where id=?", Arrays.asList(processInstance.getId()));
		
		// 删除任务
		List<HistoryTask> tasks = SessionContext.getSqlSession().query(HistoryTask.class, "select id, taskinst_id from bpm_hi_task where procinst_id=?", Arrays.asList(processInstance.getProcinstId()));
		SessionContext.getSqlSession().executeUpdate("delete bpm_hi_task where procinst_id=?", Arrays.asList(processInstance.getProcinstId()));
		
		// 删除变量
		SessionContext.getSqlSession().executeUpdate("delete bpm_hi_variable where procinst_id=?", Arrays.asList(processInstance.getProcinstId()));
		
		// 删除指派信息
		SessionContext.getSQLSession().executeUpdate("Delete4PhysicalProcess", "deleteHistoryAssignee", tasks);
		
		// 删除调度信息
		SessionContext.getSQLSession().executeUpdate("Delete4PhysicalProcess", "deleteHistoryDispatch", tasks);
		
		// 删除抄送信息
		SessionContext.getSQLSession().executeUpdate("Delete4PhysicalProcess", "deleteHistoryCC", tasks);
		
		return Result.getDefaultSuccessInstance();
	}
}
