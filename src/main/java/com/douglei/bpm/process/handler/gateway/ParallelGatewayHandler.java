package com.douglei.bpm.process.handler.gateway;

import java.util.Arrays;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.runtime.task.Task;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class ParallelGatewayHandler extends AbstractGatewayHandler{

	@Override
	public ExecutionResult startup() {
		removeVariables();
		
		Task task = createTask(false);
		if(task.getParentTaskinstId() == null) { // fork
			handleParameter.addTask(task);
			for(FlowMetadata flow : taskMetadata.getFlows()) 
				beanInstances.getTaskHandlerUtil().dispatch(flow, handleParameter);
			SessionContext.getTableSession().save(task);
		}else {
			int count = Integer.parseInt(
					SessionContext.getSqlSession().uniqueQuery_(
							"select count(1) from bpm_ru_task where procinst_id=? and parent_taskinst_id=?", 
							Arrays.asList(task.getProcinstId(), task.getParentTaskinstId()))[0].toString());
			if(count == 0) { // 可以fork
				
			}else { // 等待合并
				
			}
		}
		
		// 保存任务
		// 看看有没有并的, 并完了没有
		// 并完了就去启动多个子任务
		
		
		/*
		 * 先并再分
		 * 如果没得并了, 那就是分
		 * 
		 */
		
		/*
		 * 分支逻辑:
		 * 1. 保存并行网关任务
		 * 2. 启动flow流出的子任务, 存在parentTaskinstId 
		 * 3. 结束
		 */
		
		/*
		 * 合并逻辑:
		 * 1. 保存并行网关任务,
		 * 2. 判断是否能结束, 即根据parentTaskinstId查询, 还有多少任务没有完成
		 * 3. 如果查询结果是都完成, 则结束, 进入下一个任务
		 *  
		 */
		
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	@Override
	public ExecutionResult handle() {
		completeTask();
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
