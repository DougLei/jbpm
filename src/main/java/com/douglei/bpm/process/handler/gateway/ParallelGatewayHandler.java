package com.douglei.bpm.process.handler.gateway;

import java.util.LinkedList;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.handler.TaskDispatchException;
import com.douglei.bpm.process.handler.TaskEntity;
import com.douglei.bpm.process.handler.TaskHandleException;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.orm.context.SessionContext;

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
		
		ParallelTaskHandler parallelTaskHandler = new ParallelTaskHandler(currentTaskMetadataEntity, handleParameter.getTaskEntityHandler().getPreviousTaskEntity());
		if(parallelTaskHandler.join())  
			fork(parallelTaskHandler.getCurrentTaskParentTaskinstId());
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	
	// 进行fork操作
	private void fork(String parentTaskinstId) {
		List<FlowMetadata> outputFlows = currentTaskMetadataEntity.getOutputFlows();
		if(outputFlows.size() == 1) {
			// 没有fork的必要, 所以直接创建历史任务, 并调度
			createHistoryTask(parentTaskinstId);
			dispatch(outputFlows.get(0));
		}else {
			Task currentTask = createTask(true, parentTaskinstId, false);
			
			LinkedList<TaskEntity> historyTaskEntities = handleParameter.getTaskEntityHandler().getHistoryTaskEntities(); // 历史办理的任务实体实例集合
			int mark = historyTaskEntities.size(); // 进行初始位置标记
			for (FlowMetadata flow : outputFlows) {
				if(dispatch(flow)) {
					// 调度成功后, 重置到标记的位置
					while(historyTaskEntities.size() > mark) 
						handleParameter.getTaskEntityHandler().setCurrentTaskEntity(historyTaskEntities.removeLast());
				}
			}
			currentTask.setChildrenNum(childrenNum);
			SessionContext.getTableSession().save(currentTask);
		}
		if(childrenNum == 0)
			throw new TaskDispatchException("执行"+currentTaskMetadataEntity.getTaskMetadata()+"任务时, 未能匹配到任何满足条件的Flow");
	}
	
	// 子任务数量
	private int childrenNum; 
	// 进行调度, 返回是否调度成功
	private boolean dispatch(FlowMetadata flow) {
		if(ignoreFlowCondition() || beanInstances.getTaskHandlerUtil().flowMatching(flow, handleParameter.getVariableEntities().getVariableMap())) {
			beanInstances.getTaskHandlerUtil().dispatch(flow, handleParameter);
			childrenNum++;
			return true;
		}
		return false;
	}
	
	@Override
	public ExecutionResult handle() {
		throw new TaskHandleException(ParallelGatewayHandler.class.getName() + " 不支持handle()方法");
	}
}
