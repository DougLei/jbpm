package com.douglei.bpm.process.handler.gateway;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayHandler extends AbstractGatewayHandler{
	private static final Object key = new Object();
	private String parentTaskinstId;
	
	@Override
	public ExecutionResult startup() {
		removeVariables();
		
		if(joinHandle()) 
			handle();
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 进行join操作
	private boolean joinHandle() {
		String parentTaskinstId = handleParameter.getPreviousTask().getParentTaskinstId();
		if(parentTaskinstId == null)
			return true;
		
		synchronized (key) {
			List<Task> tasks = SessionContext.getTableSession().query(
					Task.class, 
					"select key_ from bpm_ru_task where parent_taskinst_id=?", 
					Arrays.asList(parentTaskinstId)); // 查询指定taskinstId的并行网关, 目前还未完成的(并行)任务
			if(!tasks.isEmpty()) {
				List<FlowMetadata> inputFlows = currentTaskMetadataEntity.getInputFlows();
				if(inputFlows.size() > 1) {
					for (Task task : tasks) {
						for (FlowMetadata flow : inputFlows) {
							if(task.getKey().equals(flow.getSource())) {
								return false;
							}
						}
					}
				}
			}
			
			Task parallelGatewayTask = SessionContext.getTableSession().uniqueQuery(Task.class, "select * from bpm_ru_task where taskinst_id=?", Arrays.asList(parentTaskinstId));
			if(tasks.isEmpty()) { // 所有相同parentTaskinstId的任务都已经完成
				completeTask(parallelGatewayTask);
				this.parentTaskinstId = parallelGatewayTask.getParentTaskinstId();
			}else {  
				this.parentTaskinstId = parallelGatewayTask.getTaskinstId();
			}
			return true;
		}
	}
	
	@Override
	public ExecutionResult handle() {
		// 进行fork操作
		List<FlowMetadata> outputFlows = currentTaskMetadataEntity.getOutputFlows();
		if(outputFlows.size() == 1) {
			// 没有fork的必要, 所以直接创建历史任务即可
			createHistoryTask(parentTaskinstId);
			beanInstances.getTaskHandlerUtil().dispatch(outputFlows.get(0), handleParameter);
		}else {
			createTask(parentTaskinstId);
			for (FlowMetadata flow : outputFlows) 
				beanInstances.getTaskHandlerUtil().dispatch(flow, handleParameter);
		}
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
