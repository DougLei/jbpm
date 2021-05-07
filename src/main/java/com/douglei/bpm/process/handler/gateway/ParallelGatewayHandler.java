package com.douglei.bpm.process.handler.gateway;

import java.util.LinkedList;
import java.util.List;

import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.command.dispatch.impl.SettargetDispatchExecutor;
import com.douglei.bpm.module.execution.task.runtime.Task;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.handler.TaskEntity;
import com.douglei.bpm.process.mapping.metadata.flow.FlowMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayHandler extends AbstractGatewayHandler{
	
	@Override
	public Result startup() {
		removeVariables();
		
		Task joinTask = new ParallelTaskHandler(currentTaskMetadataEntity, handleParameter.getTaskEntityHandler().getPreviousTaskEntity()).join();
		if(joinTask != null) 
			fork(joinTask);
		return Result.getDefaultSuccessInstance();
	}
	
	@Override
	public Result handle() {
		Task task = handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask();
		task.setUserId(handleParameter.getUserEntity().getUserId());
		task.setReason(handleParameter.getUserEntity().getReason());
		
		fork(task);
		return CANNOT_DISPATCH;
	}
	
	/**
	 * 进行fork操作
	 * @param gatewayTask 要进行fork的网关任务实例
	 */
	private void fork(Task gatewayTask) {
		List<FlowMetadata> outputFlows = currentTaskMetadataEntity.getOutputFlows();
		
		// 只有一条输出flow, 不进行分支处理
		if(outputFlows.size() == 1) {
			if(!flowMatching(outputFlows.get(0))) 
				throw new TaskDispatchException("执行"+currentTaskMetadataEntity.getTaskMetadata()+"任务时, 未能匹配到满足条件的OutputFlow");
			
			gatewayTask.setForkBranchNum(1);
			completeTask(gatewayTask, handleParameter.getVariableEntities());
			handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(gatewayTask, false));
			
			new SettargetDispatchExecutor(outputFlows.get(0).getTarget())
				.setParameters(currentTaskMetadataEntity, handleParameter, processEngineBeans)
				.execute();
			return;
		}
		
		// 有多条输出flow, 进行分支处理
		for(int i=0;i<outputFlows.size();i++) {
			if(!flowMatching(outputFlows.get(i)))
				outputFlows.remove(i--);
		}
		
		if(outputFlows.isEmpty()) {
			FlowMetadata defaultOutputFlow = currentTaskMetadataEntity.getDefaultOutputFlow();
			if(defaultOutputFlow == null)
				throw new TaskDispatchException("执行"+currentTaskMetadataEntity.getTaskMetadata()+"任务时, 未能匹配到满足条件的OutputFlow, 无法进行fork");
			outputFlows.add(defaultOutputFlow);
		}
		
		gatewayTask.setForkBranchNum(outputFlows.size());
		if(gatewayTask.getId() == 0) {
			SessionContext.getTableSession().save(gatewayTask);
		}else {
			SessionContext.getTableSession().update(gatewayTask);
		}
		handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(gatewayTask, true));
		
		LinkedList<TaskEntity> historyTaskEntities = handleParameter.getTaskEntityHandler().getHistoryTaskEntities(); // 历史办理的任务实体实例集合
		int mark = historyTaskEntities.size(); // 标记初始位置
		for (FlowMetadata flow : outputFlows) {
			new SettargetDispatchExecutor(flow.getTarget())
				.setParameters(currentTaskMetadataEntity, handleParameter, processEngineBeans)
				.execute();
			
			while(historyTaskEntities.size() > mark) 
				handleParameter.getTaskEntityHandler().setCurrentTaskEntity(historyTaskEntities.removeLast());
		}
	}
	
	// 进行flow匹配, 返回是否匹配成功, 即是否可以流入该flow
	protected boolean flowMatching(FlowMetadata flow) {
		return true;
	}
}
