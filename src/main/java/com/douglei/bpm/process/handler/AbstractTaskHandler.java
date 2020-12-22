package com.douglei.bpm.process.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.history.task.HistoryAssignee;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.components.assignee.AssignerFactory;
import com.douglei.bpm.process.handler.components.assignee.HistoryAssigneeHandler;
import com.douglei.bpm.process.handler.components.scheduler.TaskDispatchParameter;
import com.douglei.bpm.process.handler.components.scheduler.TaskScheduler;
import com.douglei.bpm.process.handler.components.variable.VariableHandler;
import com.douglei.orm.context.SessionContext;

/**
 * 任务处理器
 * @author DougLei
 */
public abstract class AbstractTaskHandler {
	
	@Autowired
	protected TaskScheduler taskScheduler;
	
	@Autowired
	protected VariableHandler variableHandler;
	
	@Autowired
	protected AssignerFactory assignerFactory;
	
	/**
	 * 完成任务
	 * @param executeParameter
	 * @return 
	 */
	protected final TaskDispatchParameter completeTask(GeneralExecuteParameter executeParameter) {
		Task currentTask = executeParameter.getTaskInstance();
		
		// 从运行任务表中删除任务
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_task where id = ?", Arrays.asList(currentTask.getId()));	

		// 将任务存到历史表	
		HistoryTask historyTask = new HistoryTask(currentTask);	
		SessionContext.getTableSession().save(historyTask);	
		
		// 处理指派表信息
		List<HistoryAssignee> historyAssigneeList = new HistoryAssigneeHandler(currentTask.getTaskinstId()).getHistoryAssigneeList();
		if(!historyAssigneeList.isEmpty()) {
			SessionContext.getTableSession().save(historyAssigneeList);
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_assignee where taskinst_id=?", Arrays.asList(currentTask.getTaskinstId()));
		}
		
		Map<String, Object> variableMap = variableHandler.followTaskDispatch(currentTask.getProcinstId(), currentTask.getTaskinstId());
		return new TaskDispatchParameter(currentTask.getProcinstId(), variableMap, executeParameter);
	}
}
