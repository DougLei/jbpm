package com.douglei.bpm.module.runtime.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.entity.HistoryTask;
import com.douglei.bpm.module.history.task.entity.HistoryVariable;
import com.douglei.bpm.module.runtime.task.entity.Task;
import com.douglei.bpm.module.runtime.variable.ProcessVariableMapHandler;
import com.douglei.bpm.module.runtime.variable.entity.Variable;
import com.douglei.bpm.process.container.ProcessContainerProxy;
import com.douglei.bpm.process.scheduler.ProcessScheduler;
import com.douglei.bpm.process.scheduler.flow.FlowDispatchParameter;
import com.douglei.orm.context.SessionContext;

/**
 * 完成任务命令
 * @author DougLei
 */
public class CompleteTaskCommand implements Command<ExecutionResult> {
	private int taskId;
	private List<Object> taskIdParams;
	
	public CompleteTaskCommand(int taskId) {
		this.taskId = taskId;
		this.taskIdParams = Arrays.asList(taskId);
	}
	
	@Autowired
	private ProcessContainerProxy processContainer;
	
	@Autowired
	private ProcessScheduler processScheduler;

	@Override
	public ExecutionResult execute() {
		Task task = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where id = ?", taskIdParams);
		if(task == null)
			return new ExecutionResult("不存在id为["+taskId+"]的任务");
		
		// 将任务从运行任务表删除
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_task where id = ?", taskIdParams);
		
		// 将任务存到历史表
		HistoryTask historyTask = new HistoryTask(task);
		SessionContext.getTableSession().save(historyTask);
		
		// 处理并获取当前任务的流程变量
		Map<String, Object> variableMap = getVariableMap(historyTask);
		
		processScheduler.dispatchFlow(processContainer.getProcess(task.getProcdefId()).getTask(task.getKey()).getFlows(), new FlowDispatchParameter(task.getProcdefId(), task.getProcinstId(), null, variableMap)); 
		return ExecutionResult.getSuccessInstance();
	}
	
	// 获取指定任务id的流程变量map集合
	private Map<String, Object> getVariableMap(HistoryTask historyTask){
		List<Variable> variables = SessionContext.getTableSession().query(Variable.class, "select * from bpm_ru_variable where task_id =? or scope='global'", taskIdParams);
		if(variables.isEmpty())
			return null;
		
		ProcessVariableMapHandler processVariableHandler = new ProcessVariableMapHandler();
		variables.forEach(variable -> {
			processVariableHandler.addVariable(variable);
		});
		
		// 将local和transient范围的变量从运行变量表删除
		if(processVariableHandler.existsLocalVariableMap() || processVariableHandler.existsTransientVariable())
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where task_id = ?", taskIdParams);
		
		// 将local变量保存到历史变量表
		if(processVariableHandler.existsLocalVariableMap()) {
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(processVariableHandler.getLocalVariableMap().size());
			processVariableHandler.getLocalVariableMap().values().forEach(processVariable -> {
				historyVariables.add(new HistoryVariable(historyTask.getProcdefId(), historyTask.getProcinstId(), historyTask.getId(), processVariable));
			});
			SessionContext.getTableSession().save(historyVariables);
		}
		return processVariableHandler.getVariableMap();
	}
}
