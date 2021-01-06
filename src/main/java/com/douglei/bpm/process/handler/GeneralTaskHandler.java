package com.douglei.bpm.process.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.history.variable.HistoryVariable;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.orm.context.SessionContext;

/**
 * 通用的任务处理器
 * <p>
 * 提供通用方法
 * @author DougLei
 */
public abstract class GeneralTaskHandler {
	
	/**
	 * 完成任务
	 * @param task
	 */
	protected final void completeTask(Task task) {
		// 从运行任务表中删除任务
		if(task.getId() > 0) // 此判断用于拦截, 同一次处理中, 运行任务到历史任务的转移
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_task where id = ?", Arrays.asList(task.getId()));	

		// 将任务存到历史表	
		HistoryTask historyTask = new HistoryTask(task);
		SessionContext.getTableSession().save(historyTask);	
	}
	
	/**
	 * 跟随任务完成流程变量
	 * @param task
	 */
	protected final void followTaskCompleted4Variable(Task task) {
		VariableEntities variableEntities = new VariableEntities(SessionContext.getTableSession()
				.query(Variable.class, 
						"select * from bpm_ru_variable where procinst_id=? and taskinst_id=?", Arrays.asList(task.getProcinstId(), task.getTaskinstId())));
		
		// 将local和transient范围的变量从运行变量表删除	
		if(variableEntities.existsLocalVariable() || variableEntities.existsTransientVariable())	
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where taskinst_id = ?", Arrays.asList(task.getTaskinstId()));	
		
		// 将local范围的变量保存到历史变量表	
		if(variableEntities.existsLocalVariable()) {	
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(variableEntities.getLocalVariableMap().size());	
			variableEntities.getLocalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(task.getProcinstId(), task.getTaskinstId(), variableEntity));	
			});	
			SessionContext.getTableSession().save(historyVariables);	
		}	
	}
}