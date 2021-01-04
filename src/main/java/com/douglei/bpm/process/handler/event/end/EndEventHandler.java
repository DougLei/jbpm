package com.douglei.bpm.process.handler.event.end;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.instance.HistoryProcessInstance;
import com.douglei.bpm.module.history.variable.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.variable.Variable;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.bpm.process.metadata.event.EndEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class EndEventHandler extends TaskHandler<EndEventMetadata, HandleParameter> {

	@Override
	public ExecutionResult startup() {
		return handle();
	}

	@Override
	public ExecutionResult handle() {
		// 创建流程任务(因为是结束事件, 所以直接创建历史任务结束即可)
		createHistoryTask(); // TODO 应该不在这里结束, EndEvent应该具有聚合功能
		if(isFinished()) 
			finishProcessInstance();
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 判断流程是否结束
	private boolean isFinished() {
		if(handleParameter.getPreviousTaskEntity().getTask().getParentTaskinstId() == null)
			return true;
		
		int taskCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_task where is_virtual=0 and procinst_id=?", 
				Arrays.asList(handleParameter.getProcessInstanceId()))[0].toString());
		return taskCount == 0;
	}
	
	// 结束流程实例
	private void finishProcessInstance() {
		// 将实例保存到历史
		List<Object> procinstId = Arrays.asList(handleParameter.getProcessInstanceId());
		ProcessInstance processInstance = SessionContext.getTableSession().uniqueQuery(ProcessInstance.class, "select * from bpm_ru_procinst where procinst_id=?", procinstId);
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_procinst where procinst_id=?", procinstId);
		SessionContext.getTableSession().save(new HistoryProcessInstance(processInstance, null));
		
		// 保存流程变量
		saveVariables();
	}

	// 保存流程变量
	private void saveVariables() {
		String processInstanceId = handleParameter.getProcessInstanceId();
		VariableEntities variableEntities = new VariableEntities(SessionContext.getTableSession().query(
				Variable.class, "select * from bpm_ru_variable where procinst_id=?", Arrays.asList(processInstanceId)));
		
		// 删除所有流程变量
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where procinst_id = ?", Arrays.asList(processInstanceId));	
		
		// 将global范围的变量保存到历史表
		if(variableEntities.existsGlobalVariable()) {
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(variableEntities.getGlobalVariableMap().size());
			variableEntities.getGlobalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(processInstanceId, null, variableEntity));	
			});
			SessionContext.getTableSession().save(historyVariables);
		}
		
		if(variableEntities.existsLocalVariable())
			throw new TaskDispatchException("结束流程时, 还存在local范围的变量");
	}
}
