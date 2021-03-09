package com.douglei.bpm.process.handler.event.end;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.instance.HistoryProcessInstance;
import com.douglei.bpm.module.history.variable.HistoryVariable;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.module.runtime.instance.ProcessInstanceState;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.handler.VariableEntities;
import com.douglei.bpm.process.handler.gateway.ParallelTaskHandler;
import com.douglei.bpm.process.mapping.metadata.event.EndEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class EndEventHandler extends TaskHandler<EndEventMetadata, HandleParameter> {
	
	@Override
	public ExecutionResult startup() {
		Task joinTask = new ParallelTaskHandler(currentTaskMetadataEntity, handleParameter.getTaskEntityHandler().getPreviousTaskEntity(), handleParameter.getCurrentDate()).join();
		if(joinTask != null) 
			end(joinTask);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public ExecutionResult handle() {
		Task task = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		task.setUserId(handleParameter.getUserEntity().getCurrentHandleUser().getUserId());
		task.setReason(handleParameter.getUserEntity().getReason());
		
		end(task);
		return CANNOT_DISPATCH;
	}
	
	/**
	 * 进行end操作
	 * @param endTask
	 */
	private void end(Task endTask) {
		completeTask(endTask, handleParameter.getCurrentDate(), handleParameter.getVariableEntities());
		
		if(isAllFinished()) 
			finishProcessInstance();
	}
	
	// 判断流程所有任务是否都结束
	private boolean isAllFinished() {
		if(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getParentTaskinstId() == null
				&& !handleParameter.getTaskEntityHandler().getPreviousTaskEntity().isCreateBranch())
			return true;
		
		int taskCount = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_(
				"select count(id) from bpm_ru_task where procinst_id=?", 
				Arrays.asList(handleParameter.getProcessInstanceId()))[0].toString());
		return taskCount == 0;
	}
	
	// 结束流程实例
	private void finishProcessInstance() {
		// 将实例保存到历史
		List<Object> procinstId = Arrays.asList(handleParameter.getProcessInstanceId());
		ProcessInstance processInstance = SessionContext.getTableSession().uniqueQuery(ProcessInstance.class, "select * from bpm_ru_procinst where procinst_id=?", procinstId);
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_procinst where procinst_id=?", procinstId);
		SessionContext.getTableSession().save(new HistoryProcessInstance(processInstance, ProcessInstanceState.FINISHED, null));
		
		// 保存流程变量
		saveVariables();
	}

	// 保存流程变量
	private void saveVariables() {
		String processInstanceId = handleParameter.getProcessInstanceId();
		VariableEntities variableEntities = handleParameter.getVariableEntities();
		
		// 将global范围的变量保存到历史表
		if(variableEntities.existsGlobalVariable()) {
			List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>(variableEntities.getGlobalVariableMap().size());
			variableEntities.getGlobalVariableMap().values().forEach(variableEntity -> {	
				historyVariables.add(new HistoryVariable(processInstanceId, null, variableEntity));	
			});
			SessionContext.getTableSession().save(historyVariables);
		}
		
		// 删除所有流程变量
		SessionContext.getSqlSession().executeUpdate("delete bpm_ru_variable where procinst_id = ?", Arrays.asList(processInstanceId));	
	}
}
