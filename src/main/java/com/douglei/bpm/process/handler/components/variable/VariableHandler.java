package com.douglei.bpm.process.handler.components.variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.history.variable.HistoryVariable;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.orm.context.SessionContext;

/**
 * 变量处理器
 * @author DougLei
 */
@Bean
public class VariableHandler {
	
	// 获取指定流程实例id的变量map持有对象, 即当前任务所有的变量
	private VariableMapHolder getVariableMapHolder(String procinstId) {
		List<Variable> variables = SessionContext.getTableSession().query(Variable.class, "select * from bpm_ru_variable where procinst_id=?", Arrays.asList(procinstId));	
		if(variables.isEmpty())	
			return null;
		
		VariableMapHandler variableMapHandler = new VariableMapHandler();	
		variables.forEach(variable -> variableMapHandler.addVariable(variable));
		return variableMapHandler;
	}
	
	/**
	 * 跟随任务调度
	 * @param currentTask
	 * @param historyTask
	 * @return 当前任务的变量map集合
	 */
	public Map<String, Object> followTaskDispatch(Task currentTask, int historyTask) {
		VariableMapHolder variableMapHolder = getVariableMapHolder(currentTask.getProcinstId());
		if(variableMapHolder == null)
			return null;
		
		// 将Local和Transient范围的变量从运行变量表删除	
		if(variableMapHolder.existsLocalVariableMap() || variableMapHolder.existsTransientVariable())	
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where task_id = ?", Arrays.asList(currentTask.getId()));	
		
		// 将Local范围的变量保存到历史变量表	
		if(variableMapHolder.existsLocalVariableMap()) {	
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(variableMapHolder.getLocalVariableMap().size());	
			variableMapHolder.getLocalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(currentTask.getProcinstId(), historyTask, variableEntity));	
			});	
			SessionContext.getTableSession().save(historyVariables);	
		}	
		return variableMapHolder.getVariableMap();
	}

	/**
	 * 开始调度, 用在StartEvent
	 * @param procinstId
	 * @param startEventId 
	 * @param variableMapHolder
	 * @return StartEvent的变量map集合
	 */
	public Map<String, Object> startDispatch(String procinstId, int startEventId, VariableMapHolder variableMapHolder) {
		// 保存Global范围的变量到运行表
		if(variableMapHolder.existsGlobalVariableMap()) {
			List<Variable> variables = new ArrayList<Variable>(variableMapHolder.getGlobalVariableMap().size());
			variableMapHolder.getGlobalVariableMap().values().forEach(processVariable -> {
				variables.add(new Variable(procinstId, null, processVariable));
			});
			SessionContext.getTableSession().save(variables);
		}
		
		// 保存Local范围的变量到历史表
		if(variableMapHolder.existsLocalVariableMap()) {
			List<HistoryVariable> variables = new ArrayList<HistoryVariable>(variableMapHolder.getLocalVariableMap().size());
			variableMapHolder.getLocalVariableMap().values().forEach(processVariable -> {
				variables.add(new HistoryVariable(procinstId, startEventId, processVariable));
			});
			SessionContext.getTableSession().save(variables);
		}
		return variableMapHolder.getVariableMap();
	}
	
	/**
	 * 结束调度, 用在EndEvent
	 * @param procinstId
	 */
	public void endDispatch(String procinstId) {
		VariableMapHolder variableMapHolder = getVariableMapHolder(procinstId);
		if(variableMapHolder == null)
			return;
		
		// 删除所有流程变量
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where procinst_id = ?", Arrays.asList(procinstId));	
		
		// 将Global和Local范围的变量保存到历史表
		List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>();
		if(variableMapHolder.existsGlobalVariableMap()) {
			variableMapHolder.getGlobalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(procinstId, null, variableEntity));	
			});
		}
		if(variableMapHolder.existsLocalVariableMap()) {	
			variableMapHolder.getLocalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(procinstId, variableEntity.getTaskId(), variableEntity));	
			});	
		}	
		if(!historyVariables.isEmpty())
			SessionContext.getTableSession().save(historyVariables);
	}
}
