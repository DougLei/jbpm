package com.douglei.bpm.process.handler.gateway;

import java.util.LinkedList;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.handler.TaskEntity;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayHandler extends AbstractGatewayHandler{
	private static final Object key = new Object();
	private String parentTaskinstId;
	private int outFlowCount; // 流出的flow的数量
	
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
		
		if(joinHandle()) 
			handle();
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 进行join操作
	private boolean joinHandle() {
		String parentTaskinstId = handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getParentTaskinstId();
		if(parentTaskinstId == null) {
			if(handleParameter.getTaskEntityHandler().getPreviousTaskEntity().isCreateBranch()) // 如果上一个任务会创建分支, 则这里要记录上一个任务的实例id, 作为当前任务的父任务, 例如两个并行网关连在一起
				this.parentTaskinstId = handleParameter.getTaskEntityHandler().getPreviousTaskEntity().getTask().getTaskinstId();
			return true;
		}
		
		synchronized (key) {
			ParallelTaskHandleSituation situation = new ParallelTaskHandleSituation(parentTaskinstId);
			if(situation.invalid())
				return false;
			
			if(situation.isAllCompleted()) 
				completeTask(situation.getParentParallelGatewayTask());
			
			
			
			
			return true;
		}
	}
	
	@Override
	public ExecutionResult handle() {
		// 进行fork操作
		List<FlowMetadata> outputFlows = currentTaskMetadataEntity.getOutputFlows();
		if(outputFlows.size() == 1) {
			// 没有fork的必要, 所以直接创建历史任务, 并调度
			createHistoryTask(parentTaskinstId);
			dispatch(outputFlows.get(0));
		}else {
			createTask(true, parentTaskinstId);
			
			LinkedList<TaskEntity> historyTaskEntities = handleParameter.getTaskEntityHandler().getHistoryTaskEntities(); // 历史办理的任务实体实例集合
			int mark = historyTaskEntities.size(); // 进行初始位置标记
			for (FlowMetadata flow : outputFlows) {
				if(dispatch(flow)) {
					// 调度成功后, 重置到标记的位置
					while(historyTaskEntities.size() > mark) 
						handleParameter.getTaskEntityHandler().setCurrentTaskEntity(historyTaskEntities.removeLast());
				}
			}
		}
		
		if(outFlowCount == 0)
			throw new TaskDispatchException("执行"+currentTaskMetadataEntity.getTaskMetadata()+"任务时, 未能匹配到任何满足条件的Flow");
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	// 进行调度, 返回是否调度成功
	private boolean dispatch(FlowMetadata flow) {
		if(ignoreFlowCondition() || beanInstances.getTaskHandlerUtil().flowMatching(flow, handleParameter.getVariableEntities().getVariableMap())) {
			beanInstances.getTaskHandlerUtil().dispatch(flow, handleParameter);
			outFlowCount++;
			return true;
		}
		return false;
	}
}
