package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.history.variable.HistoryVariable;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.orm.context.SessionContext;

/**
 * 变量调度器
 * @author DougLei
 */
@Bean
public class VariableScheduler {
	
	/**
	 * 开始调度, 用在StartEvent
	 * @param startEvent
	 * @param variableEntities
	 */
	public void startDispatch(HistoryTask startEvent, VariableEntities variableEntities) {
		// 保存global范围的变量到运行表
		if(variableEntities.existsGlobalVariable()) {
			List<Variable> variables = new ArrayList<Variable>(variableEntities.getGlobalVariableMap().size());
			variableEntities.getGlobalVariableMap().values().forEach(processVariable -> {
				variables.add(new Variable(startEvent.getProcinstId(), null, processVariable));
			});
			SessionContext.getTableSession().save(variables);
		}
		
		// 保存local范围的变量到历史表
		if(variableEntities.existsLocalVariable()) {
			List<HistoryVariable> variables = new ArrayList<HistoryVariable>(variableEntities.getLocalVariableMap().size());
			variableEntities.getLocalVariableMap().values().forEach(processVariable -> {
				variables.add(new HistoryVariable(startEvent.getProcinstId(), startEvent.getTaskinstId(), processVariable));
			});
			SessionContext.getTableSession().save(variables);
		}
	}
	
	/**
	 * 跟随任务调度
	 * @param procinstId
	 * @param taskinstId 
	 */
	public void followTaskDispatch(String procinstId, String taskinstId) {
		VariableEntities variableEntities = new VariableEntities(SessionContext.getTableSession()
				.query(Variable.class, 
						"select * from bpm_ru_variable where procinst_id=? and taskinst_id=?", Arrays.asList(procinstId, taskinstId)));
		
		// 将local和transient范围的变量从运行变量表删除	
		if(variableEntities.existsLocalVariable() || variableEntities.existsTransientVariable())	
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where taskinst_id = ?", Arrays.asList(taskinstId));	
		
		// 将local范围的变量保存到历史变量表	
		if(variableEntities.existsLocalVariable()) {	
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(variableEntities.getLocalVariableMap().size());	
			variableEntities.getLocalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(procinstId, taskinstId, variableEntity));	
			});	
			SessionContext.getTableSession().save(historyVariables);	
		}	
	}

	/**
	 * 结束调度, 用在EndEvent
	 * @param procinstId
	 */
	public void endDispatch(String procinstId) {
		VariableEntities variableEntities = new VariableEntities(SessionContext.getTableSession()
				.query(Variable.class, "select * from bpm_ru_variable where procinst_id=?", Arrays.asList(procinstId)));
		
		// 删除所有流程变量
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where procinst_id = ?", Arrays.asList(procinstId));	
		
		// 将global范围的变量保存到历史表
		if(variableEntities.existsGlobalVariable()) {
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(variableEntities.getGlobalVariableMap().size());
			variableEntities.getGlobalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(procinstId, null, variableEntity));	
			});
			SessionContext.getTableSession().save(historyVariables);
		}
		
		if(variableEntities.existsLocalVariable())
			throw new TaskDispatchException("结束流程时, 还存在local范围的变量");
	}
}
