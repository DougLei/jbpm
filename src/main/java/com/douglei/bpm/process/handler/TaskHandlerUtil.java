package com.douglei.bpm.process.handler;

import java.util.Map;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.handler.event.end.EndEventHandler;
import com.douglei.bpm.process.handler.event.start.StartEventHandler;
import com.douglei.bpm.process.handler.gateway.ExclusiveGatewayHandler;
import com.douglei.bpm.process.handler.gateway.InclusiveGatewayHandler;
import com.douglei.bpm.process.handler.gateway.ParallelGatewayHandler;
import com.douglei.bpm.process.handler.task.user.UserTaskHandler;
import com.douglei.bpm.process.metadata.TaskMetadata;
import com.douglei.bpm.process.metadata.TaskMetadataEntity;
import com.douglei.bpm.process.metadata.flow.FlowMetadata;
import com.douglei.tools.instances.ognl.OgnlHandler;
import com.douglei.tools.utils.StringUtil;

/**
 * 任务办理器工具
 * @author DougLei
 */
@Bean
@SuppressWarnings({"rawtypes", "unchecked"})
public class TaskHandlerUtil {
	
	@Autowired
	private BeanInstances beanInstances;
	
	// 创建任务办理器实例
	private TaskHandler createTaskHandleInstance(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, HandleParameter handleParameter) {
		TaskHandler taskHandler = null;
		switch(taskMetadataEntity.getTaskMetadata().getType()) {
			// 事件类型
			case START_EVENT:
				taskHandler = new StartEventHandler();
				break;
			case END_EVENT:
				taskHandler = new EndEventHandler();
				break;
			
			// 任务类型
			case USER_TASK:
				taskHandler = new UserTaskHandler();
				break;
			
			// 网关类型
			case EXCLUSIVE_GATEWAY:
				taskHandler = new ExclusiveGatewayHandler();
				break;
			case PARALLEL_GATEWAY:
				taskHandler = new ParallelGatewayHandler();
				break;
			case INCLUSIVE_GATEWAY:
				taskHandler = new InclusiveGatewayHandler();
				break;
			default:
				throw new TaskHandleException("目前还未实现["+taskMetadataEntity.getTaskMetadata().getType().getName()+"]类型的任务办理器");
		}
		taskHandler.setParameters(beanInstances, taskMetadataEntity, handleParameter);
		return taskHandler;
	}
	
	/**
	 * 启动任务
	 * @param taskMetadataEntity
	 * @param parameter
	 * @return
	 */
	public ExecutionResult startup(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, HandleParameter parameter) {
		return createTaskHandleInstance(taskMetadataEntity, parameter).startup();
	}
	
	/**
	 * 办理任务
	 * @param taskMetadataEntity
	 * @param parameter
	 * @return
	 */
	public ExecutionResult handle(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, HandleParameter parameter) {
		return createTaskHandleInstance(taskMetadataEntity, parameter).handle();
	}
	
	/**
	 * 任务调度
	 * <p>
	 *  对task中的flows进行调度, 选择第一条匹配的flow执行
	 * @param taskMetadataEntity
	 * @param parameter
	 * @throws TaskDispatchException
	 */
	public void dispatch(TaskMetadataEntity<? extends TaskMetadata> taskMetadataEntity, HandleParameter parameter) throws TaskDispatchException{
		for(FlowMetadata flow : taskMetadataEntity.getOutputFlows()) {
			if(flowMatching(flow, parameter.getVariableEntities().getVariableMap())) {
				dispatch(flow, parameter);
				return;
			}
		}
		
		FlowMetadata defaultOutputFlow = taskMetadataEntity.getDefaultOutputFlow();
		if(defaultOutputFlow == null)
			throw new TaskDispatchException("执行["+taskMetadataEntity.getTaskMetadata().getName()+"]任务时, 未能匹配到满足条件的Flow");
		dispatch(defaultOutputFlow, parameter);
	}
	
	/**
	 * flow匹配
	 * <p>
	 * 判断指定的flowMetadata是否满足流转条件
	 * @param flowMetadata
	 * @param variableMap
	 * @return 
	 */
	public boolean flowMatching(FlowMetadata flowMetadata, Map<String, Object> variableMap) {
		String conditionExpr = flowMetadata.getConditionExpr();
		if(StringUtil.isEmpty(conditionExpr))
			return true;
		if(variableMap == null) // TODO 有待调整, 具体看后续条件表达式的写法定
			return false;
		return OgnlHandler.getSingleton().getBooleanValue(conditionExpr, variableMap);
	}
	
	/**
	 * flow调度, 属于直接调度
	 * @param flowMetadata
	 * @param parameter
	 */
	public void dispatch(FlowMetadata flowMetadata, HandleParameter parameter) {
		startup(parameter.getProcessMetadata().getTaskMetadataEntity(flowMetadata.getTarget()), parameter);
	}
}
