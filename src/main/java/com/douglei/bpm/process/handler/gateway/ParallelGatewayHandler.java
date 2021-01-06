package com.douglei.bpm.process.handler.gateway;

import java.util.LinkedList;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.handler.TaskEntity;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayHandler extends AbstractGatewayHandler{
	
	/**
	 * 是否忽略flow的条件
	 * @return
	 */
	protected boolean ignoreFlowCondition() {
		return true;
	}
	
	@Override
	public ExecutionResult startup() {
		removeVariables();
		
		Task joinTask = new ParallelTaskHandler(currentTaskMetadataEntity, handleParameter.getTaskEntityHandler().getPreviousTaskEntity()).join();
		if(joinTask != null) 
			fork(joinTask);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public ExecutionResult handle() {
		fork(handleParameter.getTaskEntityHandler().getCurrentTaskEntity().getTask());
		return ExecutionResult.getDefaultSuccessInstance();
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
			completeTask(gatewayTask);
			handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(gatewayTask, false));
			beanInstances.getTaskHandlerUtil().dispatch(outputFlows.get(0), handleParameter);
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
		completeTask(gatewayTask);
		handleParameter.getTaskEntityHandler().setCurrentTaskEntity(new TaskEntity(gatewayTask, true));
		
		LinkedList<TaskEntity> historyTaskEntities = handleParameter.getTaskEntityHandler().getHistoryTaskEntities(); // 历史办理的任务实体实例集合
		int mark = historyTaskEntities.size(); // 标记初始位置
		for (FlowMetadata flow : outputFlows) {
			beanInstances.getTaskHandlerUtil().dispatch(flow, handleParameter);
			
			while(historyTaskEntities.size() > mark) 
				handleParameter.getTaskEntityHandler().setCurrentTaskEntity(historyTaskEntities.removeLast());
		}
	}
	
	// 进行flow匹配, 返回是否匹配成功, 即是否可以流入该flow
	private boolean flowMatching(FlowMetadata flow) {
		return ignoreFlowCondition() || beanInstances.getTaskHandlerUtil().flowMatching(flow, handleParameter.getVariableEntities().getVariableMap());
	}
}
