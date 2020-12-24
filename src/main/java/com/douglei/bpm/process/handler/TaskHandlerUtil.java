package com.douglei.bpm.process.handler;

import java.util.Map;

import com.douglei.bpm.bean.BeanInstances;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.process.handler.event.end.EndEventHandler;
import com.douglei.bpm.process.handler.event.start.StartEventHandler;
import com.douglei.bpm.process.handler.task.user.UserTaskHandler;
import com.douglei.bpm.process.metadata.node.TaskMetadata;
import com.douglei.bpm.process.metadata.node.flow.FlowMetadata;
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
	private TaskHandler createTaskHandleInstance(TaskMetadata taskMetadata, HandleParameter handleParameter) {
		TaskHandler taskHandler = null;
		switch(taskMetadata.getType()) {
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
			default:
				throw new TaskHandleException("目前还未实现["+taskMetadata.getType().getName()+"]类型的任务办理器");
		}
		taskHandler.setParameters(beanInstances, taskMetadata, handleParameter);
		return taskHandler;
	}
	
	/**
	 * 启动任务
	 * @param taskMetadata
	 * @param parameter
	 * @return
	 */
	public ExecutionResult startup(TaskMetadata taskMetadata, HandleParameter parameter) {
		return createTaskHandleInstance(taskMetadata, parameter).startup();
	}
	
	/**
	 * 办理任务
	 * @param taskMetadata
	 * @param parameter
	 * @return
	 */
	public ExecutionResult handle(TaskMetadata taskMetadata, HandleParameter parameter) {
		return createTaskHandleInstance(taskMetadata, parameter).handle();
	}
	
	/**
	 * 任务调度
	 * <p>
	 *  对task中的flows进行调度, 选择第一条匹配的flow执行
	 * @param taskMetadata
	 * @param parameter
	 * @throws TaskDispatchException
	 */
	public void dispatch(TaskMetadata taskMetadata, HandleParameter parameter) throws TaskDispatchException{
		for(FlowMetadata flow : taskMetadata.getFlows()) {
			if(flowMatching(flow, parameter.getVariableEntities().getVariableMap())) {
				startup(flow.getTargetTask(), parameter);
				return;
			}
		}
		throw new TaskDispatchException("执行["+taskMetadata.getName()+"]任务时, 未能匹配到满足条件的Flow");
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
		if(variableMap == null)
			throw new TaskDispatchException("在匹配Flow时, 未找到流程变量");
		return OgnlHandler.getSingleton().getBooleanValue(conditionExpr, variableMap);
	}
}
