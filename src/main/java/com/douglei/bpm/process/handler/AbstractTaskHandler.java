package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.history.task.entity.HistoryTask;
import com.douglei.bpm.module.history.task.entity.HistoryVariable;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.module.runtime.variable.VariableEntityMapHandler;
import com.douglei.bpm.process.handler.components.assignee.AssignerFactory;
import com.douglei.bpm.process.handler.components.scheduler.TaskDispatchParameter;
import com.douglei.bpm.process.handler.components.scheduler.TaskScheduler;
import com.douglei.orm.context.SessionContext;

/**
 * 任务处理器
 * @author DougLei
 */
public abstract class AbstractTaskHandler {
	
	@Autowired
	protected TaskScheduler taskScheduler;
	
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
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_task where id = ?", currentTask.getTaskIdParam());	

		// 将任务存到历史表	
		HistoryTask historyTask = new HistoryTask(currentTask);	
		SessionContext.getTableSession().save(historyTask);	
		
		Map<String, Object> variableMap = handleAndGetFinalVariableMap(currentTask, historyTask);
		return new TaskDispatchParameter(currentTask.getProcinstId(), variableMap, executeParameter);
	}
	// 处理并获取最终的流程变量Map集合
	private Map<String, Object> handleAndGetFinalVariableMap(Task currentTask, HistoryTask historyTask){ 
		List<Variable> variables = SessionContext.getTableSession().query(Variable.class, "select * from bpm_ru_variable where task_id =? or scope='global'", currentTask.getTaskIdParam());	
		if(variables.isEmpty())	
			return null;	

		VariableEntityMapHandler variableEntityMapHandler = new VariableEntityMapHandler();	
		variables.forEach(variable -> variableEntityMapHandler.addVariable(variable));	

		// 将local和transient范围的变量从运行变量表删除	
		if(variableEntityMapHandler.existsLocalVariableMap() || variableEntityMapHandler.existsTransientVariable())	
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where task_id = ?", currentTask.getTaskIdParam());	

		// 将local变量保存到历史变量表	
		if(variableEntityMapHandler.existsLocalVariableMap()) {	
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(variableEntityMapHandler.getLocalVariableMap().size());	
			variableEntityMapHandler.getLocalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(historyTask.getProcdefId(), historyTask.getProcinstId(), historyTask.getId(), variableEntity));	
			});	
			SessionContext.getTableSession().save(historyVariables);	
		}	
		return variableEntityMapHandler.getVariableMap();	
	}
	
}
